package com.quangtrader.cryptoportfoliotracker.inject

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.quangtrader.cryptoportfoliotracker.converters.Converters
import com.quangtrader.cryptoportfoliotracker.dao.CoinDao
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinEntity
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.data.roommodel.NotificationEntity
import com.quangtrader.cryptoportfoliotracker.data.roommodel.PortfolioEntity
import com.quangtrader.cryptoportfoliotracker.data.roommodel.TokenTop100
import com.quangtrader.cryptoportfoliotracker.data.roommodel.TransactionEntity

@Database(
    entities = [CoinFav::class, CoinEntity::class, PortfolioEntity::class, TransactionEntity::class, NotificationEntity::class, TokenTop100::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): CoinDao
}