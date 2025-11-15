package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.BinanceApi
import javax.inject.Inject

class BinanceRepository @Inject constructor(
    private val binanceApi: BinanceApi
) {

}