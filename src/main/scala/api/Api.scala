package com.mlh.stockman.api

import com.mlh.stockman.core.{ CoreActors, Core }
import akka.actor.{ ActorRefFactory, Props }
import spray.routing.RouteConcatenation
import akka.routing._
/**
 * The REST API layer. It exposes the REST services, but does not provide any
 * web server interface.<br/>
 * Notice that it requires to be mixed in with ``core.CoreActors``, which provides access
 * to the top-level actors that make up the system.
 */
trait Api extends RouteConcatenation {
  this: CoreActors with Core =>

  private implicit val _ = system.dispatcher

  val routes =
      new PortfolioRoute(portfolio).route ~
      new StockRoute().route ~
      new StatsRoute(system).route ~
      new Site() { override def actorRefFactory: ActorRefFactory = system }.route

  val rootService = system.actorOf(Props(new RoutedHttpService(routes)).withRouter(new RoundRobinRouter(5)))


}

