package com.mlh.stockman.core

import java.util.UUID

case class Portfolio(id: UUID, userId: UUID, name: String)
case class TickerEntry(id: UUID, portfolioId: UUID, symbol: String)
