package com.mlh.stockman

import com.typesafe.config.ConfigFactory
import com.datastax.driver.core.{ Cluster, ProtocolOptions }

object StockmanConfig {
  private val config = ConfigFactory.load()

  object HttpConfig {
    private val httpConfig = config.getConfig("http")

    lazy val interface = httpConfig.getString("interface")
    lazy val port = httpConfig.getInt("port")
  }

  object CassandraConfig {
    private val cassandraConfig = config.getConfig("db.cassandra")

    lazy val port = cassandraConfig.getInt("port")
    lazy val hosts = cassandraConfig.getString("hosts")
    lazy val db = cassandraConfig.getString("keyspace")
  }
}

trait CassandraCluster {
  def cluster: Cluster
}

class CassandraClusterImpl extends CassandraCluster {
  //def system: ActorSystem

  import StockmanConfig.CassandraConfig._

  lazy val cluster: Cluster =
    Cluster.builder().
    addContactPoints(hosts).
    withPort(port).
    withoutMetrics().
    withCompression(ProtocolOptions.Compression.SNAPPY).
    build()

}




