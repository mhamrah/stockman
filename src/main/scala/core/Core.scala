package com.mlh.stockman.core

import akka.actor.{ Props, ActorSystem }
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._

trait Core {
  implicit def system: ActorSystem
}

trait BootedCore extends Core {
  import com.mlh.stockman.StockmanConfig._

  implicit lazy val system = ActorSystem(clusterName)

  lazy val clusterListener = system.actorOf(Props[SimpleClusterListener], name = "clusterListener")

  Cluster(system).subscribe(clusterListener, classOf[ClusterDomainEvent])

  sys.addShutdownHook(system.shutdown())
}

trait CoreActors {
  this: Core =>

  val cassandra = new CassandraClient()

  cassandra.createSchema

  system.registerOnTermination({
    //cassandra.cluster.shutdown
  })

  val portfolio = system.actorOf(Props(classOf[PortfolioActor], cassandra.session), "portfolio")
}
