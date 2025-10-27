package com.quangtrader.cryptoportfoliotracker.data.api

import com.quangtrader.cryptoportfoliotracker.data.remote.GainerOrLoserCoinGeckoResponse
import com.quangtrader.cryptoportfoliotracker.data.remote.GlobalResponse
import com.quangtrader.cryptoportfoliotracker.data.remote.InfoToken
import com.quangtrader.cryptoportfoliotracker.data.remote.ResponseCategoryCoin
import com.quangtrader.cryptoportfoliotracker.data.remote.TickerResponse
import com.quangtrader.cryptoportfoliotracker.data.remote.Ticket
import com.quangtrader.cryptoportfoliotracker.data.remote.TrendingResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinGeckoTrendingApi {
    @GET("/api/v3/search/trending")
    suspend fun getListTrending(
    ): TrendingResponse

    @GET("/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=200&page=1&price_change_percentage=24h")
    suspend fun getTopGainerOrLoser(): List<GainerOrLoserCoinGeckoResponse>


    @GET("/api/v3/coins/categories")
    suspend fun getCategories(): List<ResponseCategoryCoin>


    @GET("/api/v3/coins/{id}")
    suspend fun getAllInfoToken( @Path("id") tokenId: String): InfoToken


    @GET("/api/v3/coins/{id}/tickers")
    suspend fun getExchange( @Path("id") tokenId: String): TickerResponse


    @GET("/api/v3/global")
    suspend fun getMarketCap(): GlobalResponse

}