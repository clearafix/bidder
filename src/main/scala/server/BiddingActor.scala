package server

import akka.actor.Actor
import akka.actor.Status.Success
import akka.util.Timeout
import model.{BidRequest, BidResponse}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._


class BiddingActor extends Actor{


  implicit val ec = ExecutionContext.global
  implicit val timeout = Timeout(45 seconds)

  def response(bid: BidRequest): Future[BidResponse] = {
    val promise = Promise[Either[BidResponse, Exception]]
    Future {
      bid.ads match {
        case Some(ads) => {
          val bids = ads map (ad => (ad, 5))
          println("Got a request")
          Thread.sleep(5000)
          println(s"Reply is $bids")
          BidResponse(bid.id * 2, Some(bids))
        }
        case None => BidResponse(bid.id * 2, None)
      }
    }
  }

  override def receive: Receive = {
    case bid: BidRequest => sender ! response(bid)
  }
}
