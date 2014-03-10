package com.mlh.stockman

import spray.httpx.Json4sJacksonSupport
import java.util.UUID
import com.mlh.stockman.core._
import org.json4s._
import org.json4s.jackson.JsonMethods._

package object api {
  object Json4sProtocol extends Json4sJacksonSupport {
    implicit def json4sJacksonFormats = DefaultFormats

    /* implicit object UuidJsonFormat extends JsonFormat[UUID] {
      def write(x: UUID) = JsString(x toString ())
      def read(value: JsValue) = value match {
        case JsString(x) => UUID.fromString(x)
        case x => deserializationError("Expected UUID as JsString, but got " + x)
      }
    }
    */

  }

}
