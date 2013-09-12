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

class PortfolioActorSpec extends TestKit(ActorSystem("portfoilio-actor-spec")) with FreeSpecLike with Matchers with ImplicitSender with
BeforeAndAfterAll {

  implicit val timeout: Timeout = 5.seconds

  val cluster = new CassandraClusterImpl().cluster
  val pa = system.actorOf(Props(new PortfolioActor(cluster)))

  "The Portfolio Actor" - {
    "can create portfolios" in {
      pa ! CreatePortfolio("tester")

      expectMsg("ok")

    }
  }
}
