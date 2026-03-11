package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.BingXApi
import com.quangtrader.cryptoportfoliotracker.data.remote.TradFiFilterResponse
import com.quangtrader.cryptoportfoliotracker.data.remote.TradFiResponse
import javax.inject.Inject

class TradFiRepository @Inject constructor(private val tradFiApi: BingXApi) {
    suspend fun getAllTradFi(): TradFiResponse {
        return tradFiApi.getAllTradFi()
    }

    suspend fun getTradFiByDisplayName(): TradFiFilterResponse {
        return tradFiApi.getAllTradFiByDisplayName()
    }
}