package com.quangtrader.cryptoportfoliotracker.data.roommodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryChatBotEntity(
    @PrimaryKey(autoGenerate = true) val idChat: Long = 0,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)