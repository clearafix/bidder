package server

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import model.{BidRequest, BidResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class Bidder {
  implicit val system = ActorSystem("bidder_two")
  implicit val timeout = Timeout(15 seconds)
  val biddingActor = system.actorOf(Props[BiddingActor], "bidding_actor")

  def request(bid: BidRequest): Future[Option[BidResponse]] = (biddingActor ? bid).mapTo[Option[BidResponse]]
}

object BiddingActor {
  def asyncRequest(req: BidRequest): Future[Option[BidResponse]] = {
    Future {
      req.ads match {
        case Some(ads) => {
          val bids = ads map (ad => (ad, 5))
          println("Got a request")
          Thread.sleep(5000)
          println(s"Reply is $bids")
          Some(BidResponse(req.id * 2, Some(bids)))
        }
        case None => None
      }
    }
  }
}

class BiddingActor extends Actor {

  import BiddingActor._

  override def receive: Receive = {
    case req: BidRequest => asyncRequest(req).pipeTo(sender)
  }
}