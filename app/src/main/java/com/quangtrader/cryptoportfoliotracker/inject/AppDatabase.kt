package com.quangtrader.cryptoportfoliotracker.inject

import androidx.room.Database
import androidx.room.RoomDatabase

//@Database(
//    entities = [QrCode::class],
//    version = 1,
//    exportSchema = false
//)
abstract class AppDatabase : RoomDatabase() {
   // abstract fun dao(): QrCodeService
}