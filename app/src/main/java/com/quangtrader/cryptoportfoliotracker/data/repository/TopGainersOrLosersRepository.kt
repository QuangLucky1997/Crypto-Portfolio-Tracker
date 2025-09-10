package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.CoinGeckoTrendingApi
import com.quangtrader.cryptoportfoliotracker.data.remote.GainerOrLoserCoinGeckoResponse
import javax.inject.Inject

class TopGainersOrLosersRepository @Inject constructor(private val topGainerOrLoserAPI: CoinGeckoTrendingApi){
    suspend fun getTopGainerOrLoser() : List<GainerOrLoserCoinGeckoResponse> {
        return topGainerOrLoserAPI.getTopGainerOrLoser()
    }

}