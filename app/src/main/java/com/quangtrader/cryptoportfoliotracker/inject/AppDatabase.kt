package com.quangtrader.cryptoportfoliotracker.inject

import androidx.room.Database
import androidx.room.RoomDatabase
import com.quangtrader.cryptoportfoliotracker.dao.CoinService
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinEntity
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.data.roommodel.PortfolioEntity
import com.quangtrader.cryptoportfoliotracker.data.roommodel.TransactionEntity

@Database(
    entities = [CoinFav::class , CoinEntity::class, PortfolioEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): CoinService
}