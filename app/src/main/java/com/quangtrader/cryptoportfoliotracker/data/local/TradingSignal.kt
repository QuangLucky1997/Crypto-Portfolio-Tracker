package com.quangtrader.cryptoportfoliotracker.data.local

data class TradingSignal(
    val asset: String,
    val structure: String,
    val trend: String,
    val key_levels: String,
    val candle_pattern: String,
    val signal: String,
    val signal_type: String,
    val entry: Double,
    val stop_loss: Double,
    val tp1: Double,
    val tp2: Double,
    val risk_reward: String,
    val confidence: Int
)