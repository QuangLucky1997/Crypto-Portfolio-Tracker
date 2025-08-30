package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.CoinMarketApi
import com.quangtrader.cryptoportfoliotracker.data.remote.ResponseCoinMarket
import javax.inject.Inject

class CoinRepository @Inject constructor(private val coinMarketApi: CoinMarketApi) {
    suspend fun getTokens(
        start: Int = 1,
        limit: Int = 100,
        convert: String = "USD"
    ): ResponseCoinMarket {
        return coinMarketApi.getListToken(
            start = start,
            limit = limit,
            convert = convert
        )
    }
}