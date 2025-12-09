package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.CoinMarketApi
import com.quangtrader.cryptoportfoliotracker.data.remote.InfoTokenResponse
import javax.inject.Inject

class OverviewRepository @Inject constructor(private val coinMarketApi: CoinMarketApi) {
    suspend fun getFullInfoToken(symbolData:String): InfoTokenResponse {
        return coinMarketApi.getFullInfoToken(symbolData)
    }
}