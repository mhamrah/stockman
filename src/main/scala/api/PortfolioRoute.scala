package com.mlh.stockman.api

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext
import spray.routing.Directives
import spray.httpx.Json4sJacksonSupport
import org.json4s.Formats
import org.json4s.DefaultFormats
import scala.concurrent.duration._
import spray.http.StatusCodes
import spray.httpx._

case class CreatePortfolio(name: String)

class PortfolioRoute(portfolio: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with Json4sJacksonSupport {

  implicit def json4sJacksonFormats: Formats = DefaultFormats
  implicit val timeout: Timeout = 1.second

  val route =
   path("portfolios") {
     post {
       entity(as[CreatePortfolio]) { createPortfolio =>
          complete {
            (portfolio ? "boo").map {
              case _ => "boo"
            }
          }
       }
       //handleWith { sm: SendMessage => messenger ! sm; "{}" }
     }
   }

}
