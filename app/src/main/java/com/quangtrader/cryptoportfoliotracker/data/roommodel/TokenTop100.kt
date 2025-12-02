package com.quangtrader.cryptoportfoliotracker.data.roommodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TokenTop100(
    @PrimaryKey(autoGenerate = true) var idCoin: Long,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "symbol") var symbol: String = "",
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "logo") var logo: String = ""
)