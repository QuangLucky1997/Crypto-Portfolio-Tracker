package com.quangtrader.cryptoportfoliotracker.data.roommodel

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PortfolioEntity::class,
            parentColumns = ["id"],
            childColumns = ["portfolioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("portfolioId"), Index("symbol", unique = false)]
)
data class CoinEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val portfolioId: Long,
    val symbol: String,
    val name: String,
    val logoUrl: String? = null,
    val quantity: Double,
    val avgBuyPrice: Double,
    val currentPrice: Double,
    val updatedAt: Long = System.currentTimeMillis()
)