package com.quangtrader.cryptoportfoliotracker.data.chatbot

sealed class ChatBotMessage {
    abstract val timestamp: Long

    data class User(
        val text: String,
        override val timestamp: Long = System.currentTimeMillis()
    ) : ChatBotMessage()

    data class Bot(
        val text: String,
        override val timestamp: Long = System.currentTimeMillis(),
        val isLoading: Boolean = false,
        val isError: Boolean = false
    ) : ChatBotMessage()
}
