package com.mlh.stockman

import com.mlh.stockman.core._
import com.mlh.stockman.api._
import scala.annotation.tailrec

object StockmanApp extends App {
  if (args.nonEmpty) System.setProperty("stockman.ip", args(0))

  val app = new BootedCore with CoreActors with Api with Web { }
}
