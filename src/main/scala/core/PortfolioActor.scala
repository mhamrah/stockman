package com.mlh.stockman.core

import akka.actor.{Props, Actor, ActorLogging}
import java.util.UUID
import com.datastax.driver.core.{ BoundStatement, Cluster }
import com.mlh.stockman.StockmanConfig.CassandraConfig._

object PortfolioActor {
  case class CreatePortfolio(name: String)
}

class PortfolioActor(cluster: Cluster) extends Actor with ActorLogging {
  import PortfolioActor._

  log.info("XXXXX: " + db)
  //val session = cluster.connect(db.toString)
  log.info(cluster.getMetadata.toString)
  //val preparedStatement = session.prepare("INSERT INTO portfolios(key, name) VALUES (?, ?)")

  def receive: Receive = {
    case CreatePortfolio(name) => {
    //  val rsFuture = session.executeAsync(preparedStatement.bind(UUID.randomUUID(), name))
      
      sender ! "ok"
    }
  }
}

