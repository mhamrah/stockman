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
import com.datastax.driver.core.querybuilder._
import scala.collection.JavaConversions._

class PortfolioActorSpec extends TestKit(ActorSystem("portfoilio-actor-spec")) with FreeSpecLike with Matchers with ImplicitSender with
BeforeAndAfterAll {

  val client = new CassandraClient()
  val pa = system.actorOf(Props(new PortfolioActor(client.session)))

  override def beforeAll {
    client.dropKeyspace
    client.ensureKeyspace
  }

  implicit val timeout: Timeout = 5.seconds

  "The Portfolio Actor" - {
    "can create portfolios" in {
      val userId = java.util.UUID.randomUUID()

      pa ! CreatePortfolio(userId, "portfolio1")

      expectMsgType[java.util.UUID]

      val q = QueryBuilder
                .select()
                .all()
                .from("portfolios")
                .where(QueryBuilder.eq("userId", userId))

      val result = client.session.execute(q).all()
      //val result = client.session.execute(s"SELECT * FROM portfolios WHERE userId=${userId}")

      result.size shouldEqual 1
      result(0).getUUID("userId") shouldEqual userId
      result(0).getString("name") shouldEqual "portfolio1"
    }
  }
}
