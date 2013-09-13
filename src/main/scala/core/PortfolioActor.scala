package com.mlh.stockman.core

import akka.actor.{Props, Actor, ActorLogging}
import java.util.UUID
import com.datastax.driver.core.{ BoundStatement, Cluster }
import com.mlh.stockman.StockmanConfig.CassandraConfig._
import scala.collection.JavaConversions._

object PortfolioActor {
  case class CreatePortfolio(name: String)
}

class PortfolioActor(cluster: Cluster) extends Actor with ActorLogging {
  import PortfolioActor._

  val session = cluster.connect(db)
  val preparedStatement = session.prepare("INSERT INTO portfolios(key, name) VALUES (?, ?)")

  def receive: Receive = {
    case CreatePortfolio(name) => {
      val rsFuture = session.executeAsync(preparedStatement.bind(UUID.randomUUID(), name))

      sender ! "ok"
    }
  }
}

