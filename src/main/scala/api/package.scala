package com.mlh.stockman

import spray.httpx.Json4sJacksonSupport
import java.util.UUID
import org.json4s._

package object api {
  object Json4sProtocol extends Json4sJacksonSupport {
  implicit def json4sJacksonFormats: Formats = jackson.Serialization.formats(NoTypeHints) + new UUIDFormat

  val jsonMethods = org.json4s.jackson.JsonMethods

  import jsonMethods._

  class UUIDFormat extends Serializer[UUID] {
    val UUIDClass = classOf[UUID]

    def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), UUID] = {
      case (TypeInfo(UUIDClass, _), JString(x)) => UUID.fromString(x)
    }

    def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
      case x: UUID => JString(x.toString)
    }
  }

  def toJValue[T <: AnyRef](value: T): JValue = {
    import jackson.Serialization._
    parse(write(value))
  }
  }

}
