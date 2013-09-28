package com.mlh.stockman.core

import akka.actor.{ Props, ActorSystem }

trait Core {
  implicit def system: ActorSystem
}

trait BootedCore extends Core {
  implicit lazy val system = ActorSystem("stockman")

  sys.addShutdownHook(system.shutdown())
}

trait CoreActors {
  this: Core =>

  val cassandra = new CassandraClient()
  cassandra.createSchema


  val portfolio = system.actorOf(Props(classOf[PortfolioActor], cassandra.session), "portfolio")
}
