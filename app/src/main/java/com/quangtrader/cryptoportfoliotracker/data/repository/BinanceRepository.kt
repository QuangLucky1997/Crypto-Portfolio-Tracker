package com.quangtrader.cryptoportfoliotracker.data.repository

import com.github.mikephil.charting.data.CandleEntry
import com.quangtrader.cryptoportfoliotracker.data.api.BinanceApi
import javax.inject.Inject

class BinanceRepository @Inject constructor(
    private val binanceApi: BinanceApi
) {

}