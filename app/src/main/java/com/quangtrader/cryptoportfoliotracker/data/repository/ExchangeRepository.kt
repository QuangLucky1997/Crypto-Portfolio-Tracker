package com.quangtrader.cryptoportfoliotracker.data.repository


import com.quangtrader.cryptoportfoliotracker.data.api.CoinGeckoTrendingApi
import com.quangtrader.cryptoportfoliotracker.data.remote.TickerResponse
import javax.inject.Inject

class ExchangeRepository @Inject constructor(private val coinGeckoTrendingApi: CoinGeckoTrendingApi) {
    suspend fun getExchange(idCoin:String): TickerResponse {
        return coinGeckoTrendingApi.getExchange(idCoin)
    }
}