package model

case class BidRequest(id: Int, ads: Option[List[String]])
case class BidResponse(id: Int, bids: Option[List[(String, Int)]])
