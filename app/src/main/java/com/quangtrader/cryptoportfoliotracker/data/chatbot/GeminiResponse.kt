package com.quangtrader.cryptoportfoliotracker.data.chatbot

data class GeminiResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: Content?
)
