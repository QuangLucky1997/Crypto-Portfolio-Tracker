package com.quangtrader.cryptoportfoliotracker.data.chatbot

sealed class ChatBotMessage {
    data class User(
        val text: String,
        val timestamp: Long = System.currentTimeMillis()
    ) : ChatBotMessage()

    data class Bot(
        val text: String,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val timestamp: Long = System.currentTimeMillis()
    ) : ChatBotMessage()
}
