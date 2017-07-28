package utils

import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import akka.http.scaladsl.unmarshalling.Unmarshaller._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import model.BidRequest

/**
  * Unmarshalling steps are: request (1) => extract http entity (2) => extract bytes/charset (3) =>
  * to ByteString/String (4) => use jackson object mapper to serialize string into an object
  *
  * Request comes to akka http route directive 'entity' that accepts FromRequestUnmarshaller = Unmarshaller[HttpRequest, T]
  * Trait Unmarshaller extends akka.http.javadsl.unmarshalling.Unmarshaller[A, B] which has
  * val requestToEntity: Unmarshaller[HttpRequest, RequestEntity]
  *
  * Unmarshaller object has various pre-defined methods to construct the needed type.
  *
  */
object JacksonHelper extends Mappings {
  def unmarshaller[T](implicit f: String => T): FromEntityUnmarshaller[T] = stringUnmarshaller map f
}

/**
  * All String => Type implicit conversions go to this trait
  */
trait Mappings {
  implicit val mapper: ObjectMapper = new ObjectMapper().registerModule(DefaultScalaModule)

  implicit def toBidRequest(json: String)(implicit mapper: ObjectMapper): BidRequest = mapper readValue(json, classOf[BidRequest])
}
