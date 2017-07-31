package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.typesafe.config.ConfigFactory
import model.{BidRequest, BidResponse}
import utils.JacksonHelper._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.pattern.ask

import scala.concurrent.duration._
import akka.actor._
import akka.http.scaladsl.model.StatusCodes
import akka.util.Timeout

object WebServer {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("bidder")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    implicit val um = unmarshaller[BidRequest]
    implicit val timeout = Timeout(5 seconds)

    val config = ConfigFactory.load
    val host = config.getString("bidder.host")
    val port = config.getInt("bidder.port")

    val biddingActor = system.actorOf(Props[BiddingActor], name = "biddingActor")

    val route: Route =
      get {
        pathSingleSlash {
          complete("test")
        } ~
          pathPrefix("app_status") {
            complete("healthy")
          }
      } ~
        post {
          path("bid") {
            entity(as[BidRequest]) { bid =>
              onSuccess(biddingActor ? bid) {
                case resp: BidResponse =>
                  complete(StatusCodes.OK, toJson(resp))
                case _ =>
                  complete(StatusCodes.InternalServerError)
              }
            }
          }
        }

    val bindingFuture = Http().bindAndHandle(route, host, port)

    bindingFuture.failed.foreach { ex =>
      println(ex)
    }
    bindingFuture.foreach { con =>
      println(con.localAddress)
    }
  }
}

