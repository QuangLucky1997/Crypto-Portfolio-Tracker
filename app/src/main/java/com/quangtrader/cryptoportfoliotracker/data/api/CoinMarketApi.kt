package com.quangtrader.cryptoportfoliotracker.data.api


import com.quangtrader.cryptoportfoliotracker.data.remote.CoinInfoResponse
import com.quangtrader.cryptoportfoliotracker.data.remote.ResponseCoinMarket
import com.quangtrader.cryptoportfoliotracker.utils.Constants

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CoinMarketApi {
    @GET("/v1/cryptocurrency/listings/latest")
    suspend fun getListToken(
        @Query("start") start: Int = 1,
        @Query("limit") limit: Int = 100,
        @Query("convert") convert: String = "USD",
        @Header("X-CMC_PRO_API_KEY") apiKey: String = Constants.API_KEY
    ): ResponseCoinMarket


    @GET("/v1/cryptocurrency/info")
    suspend fun getIconCoin(
        @Query("id") ids: String,
        @Header("X-CMC_PRO_API_KEY") apiKey: String = Constants.API_KEY
    ): CoinInfoResponse

}