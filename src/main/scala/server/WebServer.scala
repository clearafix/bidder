package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import model.BidRequest
import utils.JacksonHelper._

object WebServer {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("bidder")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    implicit val um = unmarshaller[BidRequest]

    val config = ConfigFactory.load
    val host = config.getString("bidder.host")
    val port = config.getInt("bidder.port")

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
              complete(s"BidResponse for received $bid")
            }
          }
        }

    val bindingFuture = Http().bindAndHandle(route, host, port)

    bindingFuture.failed.foreach { ex =>
      println(ex)
    }
  }
}

