package com.mlh.stockman.core

import com.mlh.stockman.api.Api
import akka.io.IO
import spray.can.Http

trait Web {
  this: Api with CoreActors with Core =>

  import com.mlh.stockman.StockmanConfig.HttpConfig._

  IO(Http)(system) ! Http.Bind(rootService, interface, port = port)

}
