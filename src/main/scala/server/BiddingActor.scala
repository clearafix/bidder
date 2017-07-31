package server

import akka.actor.Actor
import model.{BidRequest, BidResponse}

class BiddingActor extends Actor{

  def response(bid: BidRequest): BidResponse = {
    bid.ads match {
      case Some(ads) => {
        val bids = ads map (ad => (ad, 5))
        BidResponse(bid.id * 2, Some(bids))
      }
      case None => BidResponse(bid.id * 2, None)
    }
  }

  override def receive: Receive = {
    case bid: BidRequest => sender ! response(bid)
  }
}
