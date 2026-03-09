package com.quangtrader.cryptoportfoliotracker.data.api

import com.quangtrader.cryptoportfoliotracker.data.remote.GainerOrLoserUsingBinanceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceApi {
    @GET("api/v3/ticker/24hr")
    suspend fun getTopGainerOrLoser(): List<GainerOrLoserUsingBinanceResponse>
}