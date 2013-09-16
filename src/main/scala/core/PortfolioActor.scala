package com.mlh.stockman.core

import akka.actor.{Props, Actor, ActorLogging}
import java.util.UUID
import com.datastax.driver.core.{ BoundStatement, Session, querybuilder }
import com.mlh.stockman.StockmanConfig.CassandraConfig._
import scala.collection.JavaConversions._
import scala.util._
import querybuilder.QueryBuilder

object PortfolioActor {
  case class CreatePortfolio(userId: UUID, name: String)
  case class GetPortfolios(userId: UUID)
}

case class Portfolio(portfolioId: UUID, userId: UUID, name: String)

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
      rsFuture map { result => originalSender ! Portfolio(portfolioId, userId, name) }
    }
    case GetPortfolios(userId) => {
      val q = QueryBuilder
              .select()
              .all()
              .from("portfolios")
              .where(QueryBuilder.eq("userId", userId))

      val result = session.execute(q).all().map(row => Portfolio(row.getUUID("portfolioId"), row.getUUID("userId"), row.getString("name")))

      sender ! result

    }
  }
}

