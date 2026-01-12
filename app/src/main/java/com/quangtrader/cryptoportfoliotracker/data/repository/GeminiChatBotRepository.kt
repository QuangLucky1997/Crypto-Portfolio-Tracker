package com.quangtrader.cryptoportfoliotracker.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeminiChatBotRepository @Inject constructor(
    private val generativeModel: GenerativeModel
) {

    suspend fun analyze(prompt: String): String {
        val chatSession = generativeModel.startChat()
        val result = StringBuilder()

        chatSession.sendMessageStream(prompt).collect { chunk ->
            chunk.text?.let { result.append(it) }
        }

        if (result.isEmpty()) {
            throw IllegalStateException("Gemini returned empty response")
        }

        return result.toString()
    }
}