package com.mlh.stockman

import com.mlh.stockman.core._
import com.mlh.stockman.api._

object StockmanApp extends App with BootedCore with CoreActors with Api with Web {
  if (args.nonEmpty) System.setProperty("akka.remote.netty.tcp.port", args(0))
}
