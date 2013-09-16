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
import com.mlh.stockman.core._
import java.util.UUID
import spray.json._

object Json4sProtocol extends DefaultJsonProtocol with SprayJsonSupport {
   implicit object UuidJsonFormat extends JsonFormat[UUID] {
     def write(x: UUID) = JsString(x toString ())
     def read(value: JsValue) = value match {
       case JsString(x) => UUID.fromString(x)
       case x => deserializationError("Expected UUID as JsString, but got " + x)
     }
   }

  implicit val CreatePortofolioFormats = jsonFormat1(PortfolioCreate)
  implicit val PortfolioFormats = jsonFormat3(Portfolio)
}

class PortfolioRoute(portfolio: ActorRef)(implicit executionContext: ExecutionContext)
extends Directives {

  implicit val timeout: Timeout = 5 seconds

  import Json4sProtocol._
  import PortfolioActor._

  val userId = UUID.fromString("3d9cf4eb-4561-488f-984d-2a32a9f49e5e")
  val route =
    path("portfolios") {
      post {
        entity(as[PortfolioCreate]) { cp =>
          onSuccess((portfolio ? CreatePortfolio(userId = userId, name = cp.name)).mapTo[Portfolio]) { portfolio =>
              complete {
                StatusCodes.Created -> portfolio
              }
            }
          }
          //handleWith { sm: SendMessage => messenger ! sm; "{}" }
      } ~
      get {
        complete {
         (portfolio ? GetPortfolios(userId)).mapTo[Seq[Portfolio]]
        }
      }
    }
}
