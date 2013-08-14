package com.mlh.stockman.api

import akka.actor.ActorRef
import scala.concurrent.ExecutionContext
import spray.routing.Directives

class PortfolioRoute(implicit executionContext: ExecutionContext)
  extends Directives {

 val route =
   path("portfolios") {
     get {
       complete("hey")
       //handleWith { sm: SendMessage => messenger ! sm; "{}" }
     }
   }

}
