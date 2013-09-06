package com.mlh.stockman.api

import org.scalatest._
import org.scalatest.matchers._
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest
import akka.testkit.{ TestProbe }

class PortfolioRouteSpec extends FreeSpec with Matchers  with ScalatestRouteTest {
  "The Portfolio Route" - {
    "when creating Portfolios" - {
      "returns 201 Created when successful" in {
        val probe = new TestProbe(system)
        val pr = new PortfolioRoute(probe.ref)

        Post("/portfolios") ~> pr.route ~> check {
          status shouldEqual Created
        }
      }
    }
  }
}



