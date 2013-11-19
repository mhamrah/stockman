package com.mlh.stockman

import com.mlh.stockman.core._
import com.mlh.stockman.api._
import scala.annotation.tailrec

object StockmanApp extends App {
  if (args.nonEmpty) System.setProperty("stockman.ip", args(0))

  class Start extends BootedCore with CoreActors with Api with Web {
  }

  val app = new Start()

  commandLoop()

  @tailrec
  private def commandLoop(): Unit =
    Console.readLine() match {
      case "q" =>
        println("exiting")
        app.system.shutdown()
        return
      case _ =>
        println("Illegal Command!")
        commandLoop()
    }
}
