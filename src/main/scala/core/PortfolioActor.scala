package com.mlh.stockman.core

import akka.actor.{Props, Actor, ActorLogging}
import java.util.UUID
import com.datastax.driver.core.{ BoundStatement, Session }
import com.mlh.stockman.StockmanConfig.CassandraConfig._
import scala.collection.JavaConversions._
import scala.util._ 

object PortfolioActor {
  case class CreatePortfolio(userId: UUID, name: String)
}

class PortfolioActor(session: Session) extends Actor with ActorLogging {
  import PortfolioActor._
  import cassandra.resultset._
  
  implicit val executionContext = context.dispatcher

  val preparedStatement = session.prepare("INSERT INTO portfolios(userId, portfolioId, name) VALUES (?, ?, ?)")

  def receive: Receive = {
    case CreatePortfolio(userId, name) => {
      val portfolioId = UUID.randomUUID();

      val rsFuture = session.executeAsync(preparedStatement.bind(userId, portfolioId, name))
      val originalSender = sender
      rsFuture map { result => log.info(result.toString); originalSender ! portfolioId }
      // rsFuture onComplete {
      //   case Success(result) => originalSender ! portfolioId
      //   case Failure(ex) => log.error(ex.toString)
      // }
    }
  }
}

