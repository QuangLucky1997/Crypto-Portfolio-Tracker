package com.quangtrader.cryptoportfoliotracker.inject

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.quangtrader.cryptoportfoliotracker.converters.Converters
import com.quangtrader.cryptoportfoliotracker.dao.CoinDao
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.data.roommodel.HistoryChatBotEntity

@Database(
    entities = [CoinFav::class,HistoryChatBotEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): CoinDao
}