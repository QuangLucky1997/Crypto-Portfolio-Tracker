package com.quangtrader.cryptoportfoliotracker.inject

import androidx.room.Database
import androidx.room.RoomDatabase
import com.quangtrader.cryptoportfoliotracker.dao.CoinService
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav

@Database(
    entities = [CoinFav::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): CoinService
}