package com.quangtrader.cryptoportfoliotracker.data.roommodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val symbol: String,
    val targetPrice: Double,
    val direction: AlertDirection,  // ABOVE / BELOW
    val isTriggered: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class AlertDirection { ABOVE, BELOW }