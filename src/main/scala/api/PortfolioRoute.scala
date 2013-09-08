package com.mlh.stockman.api

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext
import spray.routing.Directives
import scala.concurrent.duration._
import spray.http.StatusCodes
import spray.httpx.marshalling._
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport
import scala.util._

object Json4sProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val PortofolioFormats = jsonFormat1(CreatePortfolio)
}

class PortfolioRoute(portfolio: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives {

  implicit val timeout: Timeout = 5.seconds

  import Json4sProtocol._

  val route =
   path("portfolios") {
     post {
       entity(as[CreatePortfolio]) { createPortfolio =>
         onSuccess(portfolio ? createPortfolio) { portfolioId =>
                complete {
                  StatusCodes.Created -> portfolioId.toString // portfolioId
                }
            }
       }
       //handleWith { sm: SendMessage => messenger ! sm; "{}" }
     }
   }

}
