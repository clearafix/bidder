package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import model.{BidRequest, BidResponse}
import utils.JacksonHelper._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object WebServer {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("bidder")
    implicit val materializer = ActorMaterializer()
    implicit val timeout = Timeout(45 seconds)
    val config = ConfigFactory.load
    val host = config.getString("bidder.host")
    val port = config.getInt("bidder.port")

    implicit val um = unmarshaller[BidRequest]

    val bidder = new Bidder()

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
              entity(as[BidRequest]) { bid =>
                onComplete(bidder.request(bid)) {
                  case Success(Some(resp: BidResponse)) => complete(StatusCodes.OK, toJson(resp))
                  case Success(None) => complete(StatusCodes.NotFound)
                  case Failure(ex) => complete(StatusCodes.InternalServerError)
                }
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