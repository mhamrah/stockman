package com.mlh.stockman.core

import org.scalatest._
import org.scalatest.matchers._
import akka.actor.{ ActorSystem, Props }
import akka.pattern.ask
import akka.testkit.{ ImplicitSender, TestActorRef, TestKit }
import com.mlh.stockman._
import com.mlh.stockman.core.PortfolioActor._
import akka.util.Timeout
import scala.concurrent.duration._
import com.datastax.driver.core._
import com.datastax.driver.core.querybuilder._
import QueryBuilder._
import scala.collection.JavaConversions._

class PortfolioActorSpec extends TestKit(ActorSystem("portfoilio-actor-spec")) with FreeSpecLike with Matchers with ImplicitSender with
BeforeAndAfterAll {

  val client = new CassandraClient()
  val pa = system.actorOf(Props(new PortfolioActor(client.session)))

  override def beforeAll {
    client.dropKeyspace
    client.createSchema
  }

  implicit val timeout: Timeout = 5.seconds

  "The Portfolio Actor" - {
    "can create portfolios" in {
      val userId = java.util.UUID.randomUUID()

      pa ! CreatePortfolio(userId, "portfolio1")
      pa ! CreatePortfolio(userId, "portman")

      expectMsgType[Portfolio]
      expectMsgType[Portfolio](30 seconds)

      val q = QueryBuilder
                .select()
                .all()
                .from("portfolios")
                .where(QueryBuilder.eq("userId", userId))

      val result = client.session.execute(q).all()

      result.size shouldEqual 2
      result(0).getUUID("userId") shouldEqual userId
    }
    "can list a user's portfolios" in {
      val userId = java.util.UUID.randomUUID()

      client.session.execute(s"INSERT INTO portfolios(userId, name) VALUES (${userId}, 'portfolio test')")

      pa ! GetPortfolios(userId)

      expectMsgPF() {
        case a @ Seq(result) => {
          result.asInstanceOf[Portfolio].name shouldEqual "portfolio test"
          a.size shouldEqual 1
        }
      }

    }
  }
}
