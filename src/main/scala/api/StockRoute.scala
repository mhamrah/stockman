package com.mlh.stockman.api

import akka.actor.ActorRef
import scala.concurrent.ExecutionContext
import spray.routing.Directives

class StockRoute(implicit executionContext: ExecutionContext)
    extends Directives {

  val route =
    path("stocks") {
      get {
        complete("hey")
        //handleWith { sm: SendMessage => messenger ! sm; "{}" }
      }
    }

}

