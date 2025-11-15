package com.quangtrader.cryptoportfoliotracker.data.roommodel

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CoinEntity::class,
            parentColumns = ["id"],
            childColumns = ["coinId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("coinId")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val coinId: Long,
    val type: String,
    val quantity: Double,
    val price: Double,
    val date: Long = System.currentTimeMillis()
)