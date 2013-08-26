package com.mlh.stockman.api

import akka.actor.{Actor}
import scala.concurrent.ExecutionContext
import spray.routing.{HttpService, Directives}
import spray.util.LoggingContext

//import spray.httpx.TwirlSupport._

trait Site extends HttpService {
 val route =
   path("") { getFromResource("site/index.html") } ~
   getFromResourceDirectory("site")
}


