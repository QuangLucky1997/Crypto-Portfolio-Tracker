package com.quangtrader.cryptoportfoliotracker.data.mapper

import com.quangtrader.cryptoportfoliotracker.data.chatbot.Content
import com.quangtrader.cryptoportfoliotracker.data.chatbot.GeminiRequest
import com.quangtrader.cryptoportfoliotracker.data.chatbot.Part

object GeminiRequestBuilder {
    fun build(
        systemPrompt: String,
        userPrompt: String
    ): GeminiRequest {
        return GeminiRequest(
            contents = listOf(
                Content(
                    role = "user",
                    parts = listOf(
                        Part(text = systemPrompt),
                        Part(text = userPrompt)
                    )
                )
            )
        )
    }
}