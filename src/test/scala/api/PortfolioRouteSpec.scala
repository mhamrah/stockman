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
import scala.concurrent.duration._

class PortfolioRouteSpec extends FreeSpec with Matchers with ScalatestRouteTest {

  import Json4sProtocol._
    val portfolio = Portfolio(randomUUID(), randomUUID(), "changeMe")
    val pr = new PortfoliosRoute(actor("test")(new Act {
      import PortfolioActor._
      become {
        case CreatePortfolio(userId, name) => sender ! portfolio.copy(name = name)
        case GetPortfolios(userId) => sender ! Seq(portfolio.copy(name = "p1"), portfolio.copy(name = "p2"))
        case AddStock(portfolioId, symbol) => sender ! StockEntry(portfolioId, randomUUID(), symbol)
      }
    }))

  "The Portfolios Route" - {
    "post portfolios/" - {
      "returns 201 Created when successful" in {
        Post("/portfolios", PortfolioCreate("yabba zabba")) ~> pr.route ~> check {
          status shouldEqual Created
          responseAs[Portfolio].name shouldEqual "yabba zabba"
        }
      }
    }
    "get portfolios/" - {
      "returns a list of Portfolios" in {
        Get("/portfolios") ~> pr.route ~> check {
          status shouldEqual OK
          responseAs[Seq[Portfolio]].length shouldEqual 2
        }
      }
    }
    "post portfolios/:id/stocks" - {
      "can add a stock to a portfolio" in {
        implicit val timeout = 10.seconds
        Post(s"/portfolios/${portfolio.id}/stocks", Stock("AMAZ")) ~> pr.route ~> check {
          status shouldEqual Created
          responseAs[StockEntry].id.toString.length shouldEqual 36
        }
      }
    }
  }
}



