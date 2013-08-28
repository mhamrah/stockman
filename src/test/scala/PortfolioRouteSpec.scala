package com.mlh.stockman.api

import org.scalatest._
import org.scalatest.matchers._
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest

class PortfolioRouteSpec extends FreeSpec with Matchers  with ScalatestRouteTest {
  "The Portfolio Route" - {
    "when creating Portfolios" - {
      "returns 201 Created when successful" in {
        val pr = new PortfolioRoute()

        Post("/portfolios") ~> pr.route ~> check {
          response === OK
        }
      }
    }
  }
}



