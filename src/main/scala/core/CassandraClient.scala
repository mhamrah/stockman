package com.mlh.stockman.core

import com.datastax.driver.core.{ Cluster, ProtocolOptions, Session }
import com.datastax.driver.core.exceptions._
import com.typesafe.scalalogging.slf4j._
import com.mlh.stockman.StockmanConfig
import scala.collection.JavaConversions._

trait CassandraCluster {
  def cluster: Cluster
  val clusterSession: Session
  val session: Session
}

class CassandraClient extends CassandraCluster with StrictLogging {
  import StockmanConfig.CassandraConfig._

  lazy val clusterSession = cluster.connect()
  lazy val session = cluster.connect(db)

  lazy val cluster: Cluster =
    Cluster.builder().
      addContactPoints(hosts).
      withPort(port).
      withoutMetrics().
      withCompression(ProtocolOptions.Compression.SNAPPY).
      build()

  def createSchema = {
    try {
      clusterSession.execute(s"""CREATE KEYSPACE IF NOT EXISTS ${db} WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '2' }""")
      session.execute("""
        CREATE TABLE IF NOT EXISTS portfolios (
          userId uuid,
          name text,
          id uuid,
          PRIMARY KEY (userId, name)
        ) """)
      session.execute("""
        CREATE TABLE IF NOT EXISTS stocks (
          portfolioId uuid,
          entryId uuid,
          symbol text,
          PRIMARY KEY ( portfolioId, entryId )
        ) """)
    } catch {
      case e: Exception => logger.error("Create Schema error: " + e)
    }
  }
  def dropKeyspace = {
    try {
      clusterSession.execute(s"""DROP KEYSPACE IF EXISTS ${db}""")
    } catch {
      case e: Exception => logger.error("Drop Keyspace error: " + e)
    }
  }

  def printInfo() = {
    val md = cluster.getMetadata

    logger.info("Hosts: " + md.getAllHosts.map(h => h.toString).mkString(","))
    logger.info("Cluster: " + md.getClusterName)
    logger.info("Keyspaces: " + md.getKeyspaces.map(k => k.getName).mkString(","))
  }
}
