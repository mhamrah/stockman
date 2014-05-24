package com.mlh.stockman.directives

import spray.routing._
import java.util.UUID
import Directives._
import shapeless._
import spray.http._
import HttpHeaders._

trait UuidDirectives {
  def generateUuid: Directive[UUID :: HNil] = {
    optionalHeaderValueByName("correlation-id").flatMap {
      case Some(value) => provide(UUID.fromString(value))
      case None =>
        val id = UUID.randomUUID
        mapRequest(r => r.withHeaders(r.headers :+ RawHeader("correlation-id", id.toString))) & provide(id)
    }
  }
}
