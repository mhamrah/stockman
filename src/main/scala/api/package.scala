package com.mlh.stockman

import spray.json._
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport
import java.util.UUID
import com.mlh.stockman.core._

package object api {
  object Json4sProtocol extends DefaultJsonProtocol with SprayJsonSupport {
    implicit object UuidJsonFormat extends JsonFormat[UUID] {
      def write(x: UUID) = JsString(x toString ())
      def read(value: JsValue) = value match {
        case JsString(x) => UUID.fromString(x)
        case x => deserializationError("Expected UUID as JsString, but got " + x)
      }
    }

    implicit val CreatePortofolioFormats = jsonFormat1(PortfolioCreate)
    implicit val PortfolioFormats = jsonFormat3(Portfolio)
    implicit val StockFormats = jsonFormat1(Stock)
    implicit val StockEntryFormats = jsonFormat3(StockEntry)
  }

}
