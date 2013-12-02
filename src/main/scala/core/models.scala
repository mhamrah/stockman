package com.mlh.stockman.core

import java.util.UUID

case class Portfolio(id: UUID, userId: UUID, name: String)
case class StockEntry(id: UUID, portfolioId: UUID, symbol: String)
