package com.quangtrader.cryptoportfoliotracker.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.quangtrader.cryptoportfoliotracker.inject.AppModule
import javax.inject.Inject

class GeminiChatBotRepository @Inject constructor(
   private val generativeModel: GenerativeModel
) {

    suspend fun analyze(prompt: String): String {
        val session = generativeModel.startChat()
        val result = StringBuilder()

        try {
            session.sendMessageStream(prompt).collect { chunk ->
                result.append(chunk.text.orEmpty())
            }
        } catch (e: Exception) {
            throw e
        }

        return result.toString()
    }

}