package com.quangtrader.cryptoportfoliotracker.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeminiChatBotRepository @Inject constructor(private val generativeModel: GenerativeModel) {
    private var chatSession = generativeModel.startChat()
    fun getChatResponse(prompt: String): Flow<String> = flow {
        val responseStream = chatSession.sendMessageStream(prompt)
        responseStream.collect { chunk ->
            chunk.text?.let { emit(it) }
        }
    }
}