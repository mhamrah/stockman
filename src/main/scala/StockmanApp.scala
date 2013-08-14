package com.mlh.stockman

import com.mlh.stockman.core._
import com.mlh.stockman.api._

object StockmanApp extends App with BootedCore with CoreActors with Api with Web {
}
