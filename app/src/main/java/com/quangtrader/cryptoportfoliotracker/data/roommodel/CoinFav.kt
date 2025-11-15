package com.quangtrader.cryptoportfoliotracker.data.roommodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class CoinFav(
    @PrimaryKey(autoGenerate = true) var idCoin: Long,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "symbol") var symbol: String = "",
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "percentChange24h") var percentChange24h: Double,
    @ColumnInfo(name = "marketCap") var marketCap: Double,
    @ColumnInfo(name = "logo") var logo: String? = "",
    @ColumnInfo(name = "isFAV") var isFAV: Boolean = false,
    @ColumnInfo(name = "quantity") var quantiles: Int,
    @ColumnInfo(name = "valueToken") var valueToken: Double,
)