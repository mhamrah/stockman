package com.mlh.stockman.core

import com.mlh.stockman.api.Api
import akka.io.IO
import spray.can.Http

trait Web {
  this: Api with CoreActors with Core =>

  IO(Http)(system) ! Http.Bind(rootService, "localhost", port = 9000)

}
