package com.mlh.stockman

import com.typesafe.config.ConfigFactory
import com.datastax.driver.core.{ Cluster, ProtocolOptions }
import com.datastax.driver.core.exceptions._

object StockmanConfig {
  private val config =  ConfigFactory.load()

  private lazy val root = config.getConfig("stockman")

  lazy val clusterName = root.getString("cluster.name")
  lazy val ip = root.getString("ip")

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
