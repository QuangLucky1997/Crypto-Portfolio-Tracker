package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.NewsCryptoApi
import com.quangtrader.cryptoportfoliotracker.data.remote.Article
import com.quangtrader.cryptoportfoliotracker.data.remote.NewsByTokenResponse
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import javax.inject.Inject

class NewsByTokenRepository @Inject constructor(private val api: NewsCryptoApi) {
    suspend fun getNewsByToken(token: String): NewsByTokenResponse {
        return api.getNewsByToken(token, Constants.NEWS_API)
    }
}