package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.BinanceApi
import com.quangtrader.cryptoportfoliotracker.data.api.CoinGeckoTrendingApi
import com.quangtrader.cryptoportfoliotracker.data.remote.GainerOrLoserCoinGeckoResponse
import com.quangtrader.cryptoportfoliotracker.data.remote.GainerOrLoserUsingBinanceResponse
import javax.inject.Inject

class TopGainersOrLosersRepository @Inject constructor(private val topGainerOrLoserAPI: BinanceApi) {
    suspend fun getTopGainerOrLoser(): List<GainerOrLoserUsingBinanceResponse> {
        return topGainerOrLoserAPI.getTopGainerOrLoser()
    }
}
