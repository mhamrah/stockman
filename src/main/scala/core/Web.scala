package com.mlh.stockman.core

import com.mlh.stockman.api.Api
import akka.io.IO
import spray.can.Http
import akka.actor.ActorDSL._
import akka.actor.ActorLogging
import akka.io.Tcp._

trait Web {
  this: Api with CoreActors with Core =>

  import com.mlh.stockman.StockmanConfig.HttpConfig._

  val logger = actor(new Act with ActorLogging {
    become {
      case b @ Bound(connection) => log.info(b.toString)
      case cf @ CommandFailed(command) => log.error(cf.toString)
    }
  })

  IO(Http)(system).tell(Http.Bind(rootService, interface, port = port), logger)
}
