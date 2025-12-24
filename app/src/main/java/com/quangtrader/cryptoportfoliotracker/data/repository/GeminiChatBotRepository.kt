package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.GeminiApi
import com.quangtrader.cryptoportfoliotracker.data.chatbot.PromptConfig.SYSTEM_PROMPT_PRICE_ACTION
import com.quangtrader.cryptoportfoliotracker.data.mapper.GeminiRequestBuilder
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import javax.inject.Inject

class GeminiChatBotRepository @Inject constructor(private val geminiApi: GeminiApi) {
    suspend fun analyzeToken(prompt: String): String {
        val request = GeminiRequestBuilder.build(
            systemPrompt = SYSTEM_PROMPT_PRICE_ACTION,
            userPrompt = prompt
        )

        val response = geminiApi.generateContent(
            apiKey = Constants.API_GEMINI,
            request = request
        )

        return response.candidates
            ?.firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text
            ?: ""
    }
}