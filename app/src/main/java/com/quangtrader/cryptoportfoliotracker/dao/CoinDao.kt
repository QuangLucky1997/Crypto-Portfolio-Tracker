package com.quangtrader.cryptoportfoliotracker.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.data.roommodel.NotificationEntity
import com.quangtrader.cryptoportfoliotracker.data.roommodel.TokenTop100
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createFavCoin(coin: CoinFav): Long

    @Query("SELECT * FROM CoinFav")
    fun getAllCoinFAV(): Flow<List<CoinFav>>

    @Query("SELECT COUNT(*) FROM CoinFav WHERE name = :name")
    fun checkIfExists(name: String): Flow<Int>

    @Query("UPDATE CoinFav SET isFAV = :isFAV WHERE symbol = :symbolData")
    fun updateFavCoin(symbolData: String, isFAV: Boolean)

    @Query("DELETE FROM CoinFav WHERE symbol = :symbolData")
    fun deleteCoin(symbolData: String): Int


    @Query("SELECT * FROM CoinFav WHERE isFAV = 1")
    fun getAllFavCoin(): Flow<List<CoinFav>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveTop100Coin(token: TokenTop100): Long

    @Query("SELECT * FROM TokenTop100")
    fun getAllTop100(): Flow<List<TokenTop100>>


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAlert(alert: NotificationEntity)
//
//    @Delete
//    suspend fun deleteAlert(alert: NotificationEntity)
//
//    @Query("SELECT * FROM NotificationEntity ORDER BY createdAt DESC")
//    fun getAllAlertsFlow(): Flow<List<NotificationEntity>>
//
//
//    @Query("SELECT * FROM NotificationEntity WHERE isTriggered = 0")
//    suspend fun getActiveAlerts(): List<NotificationEntity>
//
//
//    @Query("SELECT * FROM NotificationEntity WHERE symbol = :symbol AND isTriggered = 0")
//    suspend fun getAlertsBySymbol(symbol: String): List<NotificationEntity>
//
//
//    @Query("SELECT DISTINCT symbol FROM NotificationEntity WHERE isTriggered = 0")
//    suspend fun getAllSymbols(): List<String>
//
//
//    @Query("UPDATE NotificationEntity SET isTriggered = 1 WHERE id = :id")
//    suspend fun markTriggered(id: Int)

}