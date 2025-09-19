package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.CoinGeckoTrendingApi
import com.quangtrader.cryptoportfoliotracker.data.remote.InfoToken
import javax.inject.Inject

class OverviewRepository @Inject constructor(private val coinGeckoTrendingApi: CoinGeckoTrendingApi) {
    suspend fun getOverViewInfo(id: String): InfoToken {
        return coinGeckoTrendingApi.getAllInfoToken(id)
    }
}