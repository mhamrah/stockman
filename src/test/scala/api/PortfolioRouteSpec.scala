package com.mlh.stockman.api

import org.scalatest._
import org.scalatest.matchers._
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest
import akka.testkit.{ TestProbe }
import spray.httpx.marshalling.Marshaller
import spray.http._
import akka.actor.ActorDSL._
import HttpMethods._
import HttpHeaders._
import ContentTypes._
import com.mlh.stockman.core._
import java.util.UUID._

class PortfolioRouteSpec extends FreeSpec with Matchers  with ScalatestRouteTest {

  import Json4sProtocol._

  "The Portfolio Route" - {
    "when creating Portfolios" - {
      "returns 201 Created when successful" in {
        val portfolio = Portfolio(randomUUID(), randomUUID(), "someName")
        val pr = new PortfolioRoute(actor("test")(new Act {
          become {
            case _ => sender ! portfolio
          }
        }))

        Post("/portfolios", PortfolioCreate("someName")) ~> pr.route ~> check {
          status shouldEqual Created
          entityAs[Portfolio] shouldEqual portfolio
        }
      }
    }
  }
}



