package com.quangtrader.cryptoportfoliotracker.data.chatbot

data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val role: String,
    val parts: List<Part>
)

data class Part(
    val text: String
)