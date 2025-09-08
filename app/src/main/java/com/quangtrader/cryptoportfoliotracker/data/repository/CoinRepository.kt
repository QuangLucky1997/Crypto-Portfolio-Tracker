package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.CoinMarketApi
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinInfoResponse
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
import com.quangtrader.cryptoportfoliotracker.data.remote.ResponseCoinMarket
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val coinMarketApi: CoinMarketApi
) {
    suspend fun getTokens(
        start: Int = 1,
        limit: Int = 100,
        convert: String = "USD"
    ): ResponseCoinMarket {
        return coinMarketApi.getListToken(
            start = start,
            limit = limit,
            convert = convert
        )
    }


    suspend fun getIconsForTokens(ids: List<Int>): CoinInfoResponse {
        val idsParam = ids.joinToString(",")
        return coinMarketApi.getIconCoin(idsParam)
    }


    fun mergeCoins(
        responseCoinMarket: ResponseCoinMarket,
        responseCoinInfo: CoinInfoResponse
    ): List<CoinUI> {
        val infoMap = responseCoinInfo.data ?: emptyMap()
        return responseCoinMarket.data.map { data ->
            val info = infoMap[data.id?.toString()]
            CoinUI(
                id = data.id ?: -1,
                name = data.name.orEmpty(),
                symbol = data.symbol.orEmpty(),
                price = data.quote?.USD?.price ?: 0.0,
                percentChange24h = data.quote?.USD?.percentChange24h ?: 0.0,
                marketCap = data.quote?.USD?.marketCap ?: 0.0,
                logo = info?.logo ?: ""
            )
        }
    }
}