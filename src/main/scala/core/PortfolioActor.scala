package com.mlh.stockman.core

import akka.actor.{Props, Actor}
import java.util.UUID

object PortfolioActor {

  case class CreatePortfolio(name: String)

}

class PortfolioActor extends Actor {
  import PortfolioActor._


  def receive: Receive = {
    case CreatePortfolio(name) => sender ! "ok"
    case _ => sender ! "ok"
  }
}

