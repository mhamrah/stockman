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
  case class AddStock(portfolioId: UUID, symbol: String)
}

class PortfolioActor(session: Session) extends Actor with ActorLogging {
  import PortfolioActor._
  import cassandra.resultset._

  implicit val executionContext = context.dispatcher

  val insertPortfolio = session.prepare("INSERT INTO portfolios(userId, name, id) VALUES (?, ?, ?) if not exists")
  val insertStock = session.prepare("INSERT INTO stocks(portfolioId, entryId, symbol) VALUES (?, ?, ?)")

  log.info("startup")

  def receive: Receive = {
    case AddStock(portfolioId, symbol) => {
      val entryId = UUID.randomUUID();
      val rsFuture = session.executeAsync(insertStock.bind(portfolioId, entryId, symbol))

      log.info("boot!")

      sender ! rsFuture.map { result =>
        StockEntry(entryId, portfolioId, symbol)
      }
    }
    case CreatePortfolio(userId, name) => {
      val portfolioId = UUID.randomUUID();
      val rsFuture = session.executeAsync(insertPortfolio.bind(userId, name, portfolioId))

      log.info("zzz")
      rsFuture.map { result => log.info("yyy"); Portfolio(portfolioId, userId, name) }

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

