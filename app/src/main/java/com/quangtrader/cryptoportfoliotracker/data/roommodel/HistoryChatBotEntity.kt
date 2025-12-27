package com.quangtrader.cryptoportfoliotracker.data.roommodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryChatBotEntity(
    @PrimaryKey(autoGenerate = true) val idChat: Long = 0,
    val question: String,
    val timestamp: Long = System.currentTimeMillis()
)