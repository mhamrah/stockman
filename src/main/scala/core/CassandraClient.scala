package com.mlh.stockman.core

import com.datastax.driver.core.{ Cluster, ProtocolOptions, Session }
import com.datastax.driver.core.exceptions._
import com.typesafe.scalalogging.slf4j.Logging
import com.mlh.stockman.StockmanConfig
import scala.collection.JavaConversions._

trait CassandraCluster {
  def cluster: Cluster
  val clusterSession: Session
  val session: Session
}

class CassandraClient extends CassandraCluster with Logging {
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

  def initSchema = {
    ensureKeyspace
    createSchema
  }
  def ensureKeyspace = {
    try {
      clusterSession.execute(s"""CREATE KEYSPACE IF NOT EXISTS ${db} WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '3' }""")
    } catch {
      case aee: AlreadyExistsException => logger.error("aee: " + aee)
      case e: Exception => logger.error("ex: " + e)
    }
  }
  def createSchema = {
    try {
      session.execute("""
        CREATE TABLE IF NOT EXISTS portfolios (
          userId uuid,
          name text,
          PRIMARY KEY (userId, name)
        ) WITH COMPACT STORAGE""")
    } catch {
      case e: Exception => logger.error("ex: " + e)
    }
  }
  def dropKeyspace = {
    try {
      session.execute(s"""DROP KEYSPACE IF EXISTS ${db}""")
    } catch {
      case e: Exception => logger.error("ex: " + e)
    }
  }

  def printInfo() = {
    val md = cluster.getMetadata

    logger.info("Hosts: " + md.getAllHosts.map(h => h.toString).mkString(","))
    logger.info("Cluster: " + md.getClusterName)
    logger.info("Keyspaces: " + md.getKeyspaces.map(k => k.getName).mkString(","))
  }
}
