package com.mlh.stockman.api

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext
import spray.routing.Directives
import scala.concurrent.duration._
import spray.http.StatusCodes
import spray.httpx.marshalling._
import scala.util._
import com.mlh.stockman.core._
import java.util.UUID

class PortfoliosRoute(portfolio: ActorRef)(implicit executionContext: ExecutionContext)
    extends Directives {

  implicit val timeout: Timeout = 5 seconds

  import Json4sProtocol._
  import PortfolioActor._

  val userId = UUID.fromString("3d9cf4eb-4561-488f-984d-2a32a9f49e5e")
  val route =
    path("portfolios") {
      get {
          complete {
            (portfolio ? GetPortfolios(userId)).mapTo[Seq[Portfolio]]
          }
      } ~
      post {
        entity(as[PortfolioCreate]) { cp =>
            complete {
              StatusCodes.Created -> (portfolio ? CreatePortfolio(userId = userId, name = cp.name)).mapTo[Portfolio]
            }
        }
      }
   }
}
