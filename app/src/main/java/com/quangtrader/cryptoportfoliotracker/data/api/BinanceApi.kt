package com.quangtrader.cryptoportfoliotracker.data.api

import com.github.mikephil.charting.data.CandleEntry
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceApi {
    @GET("api/v3/klines")
    suspend fun getKlines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int
    ): List<List<Any>>
}