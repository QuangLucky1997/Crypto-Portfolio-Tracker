package com.quangtrader.cryptoportfoliotracker.data.api

import com.quangtrader.cryptoportfoliotracker.data.chatbot.GeminiRequest
import com.quangtrader.cryptoportfoliotracker.data.chatbot.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GeminiApi {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String = "gemini-1.5-flash",
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}