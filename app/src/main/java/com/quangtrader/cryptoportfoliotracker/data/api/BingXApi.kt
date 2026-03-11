package com.quangtrader.cryptoportfoliotracker.data.api

import com.quangtrader.cryptoportfoliotracker.data.remote.TradFiFilterResponse
import com.quangtrader.cryptoportfoliotracker.data.remote.TradFiResponse
import retrofit2.http.GET

interface BingXApi {
    @GET("/openApi/swap/v2/quote/ticker")
    suspend fun getAllTradFi(): TradFiResponse

    @GET("/openApi/swap/v2/quote/contracts")
    suspend fun getAllTradFiByDisplayName(): TradFiFilterResponse
}