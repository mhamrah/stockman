package com.mlh.stockman.core

import akka.actor.{ Props, Actor, ActorLogging }
import java.util.UUID
import com.datastax.driver.core.{ BoundStatement, Session, querybuilder }
import com.mlh.stockman.StockmanConfig.CassandraConfig._
import scala.collection.JavaConversions._
import scala.util._
import querybuilder.QueryBuilder

object PortfolioActor {
  case class CreatePortfolio(userId: UUID, name: String)
  case class GetPortfolios(userId: UUID)
  case class AddTicker(portfolioId: UUID, symbol: String)
}

case class Portfolio(id: UUID, userId: UUID, name: String)
case class TickerEntry(id: UUID, portfolioId: UUID, symbol: String)

class PortfolioActor(session: Session) extends Actor with ActorLogging {
  import PortfolioActor._
  import cassandra.resultset._

  implicit val executionContext = context.dispatcher

  val insertPortfolio = session.prepare("INSERT INTO portfolios(userId, name, id) VALUES (?, ?, ?) if not exists")
  val insertTicker = session.prepare("INSERT INTO tickers(portfolioId, entryId, ticker) VALUES (?, ?, ?)")

  def receive: Receive = {
    case AddTicker(portfolioId, symbol) => {
      val entryId = UUID.randomUUID();
      val originalSender = sender

      val rsFuture = session.executeAsync(insertTicker.bind(portfolioId, entryId, symbol))

      rsFuture onSuccess {
        case result =>
          originalSender ! TickerEntry(entryId, portfolioId, symbol)
      }
    }
    case CreatePortfolio(userId, name) => {
      val portfolioId = UUID.randomUUID();

      val rsFuture = session.executeAsync(insertPortfolio.bind(userId, name, portfolioId))
      val originalSender = sender
      rsFuture onSuccess {
        case result => {
          originalSender ! Portfolio(portfolioId, userId, name)
        }
      }
      rsFuture onFailure {
        case f => log.error(f.toString)
      }
    }
    case GetPortfolios(userId) => {
      val q = QueryBuilder
        .select()
        .all()
        .from("portfolios")
        .where(QueryBuilder.eq("userId", userId))

      val result = session.execute(q).all().map(row => Portfolio(row.getUUID("id"), row.getUUID("userId"), row.getString("name")))

      sender ! result
    }
  }
}

