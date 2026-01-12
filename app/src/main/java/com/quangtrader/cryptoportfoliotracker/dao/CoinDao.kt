package com.quangtrader.cryptoportfoliotracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.data.roommodel.HistoryChatBotEntity
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


//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun saveHistoryChat(chatHistory: HistoryChatBotEntity): Long

//    @Query("DELETE FROM historychatbotentity WHERE idChat = :userId")
//     fun deleteChatById(userId: Int)

//    @Query("SELECT * FROM HistoryChatBotEntity")
//    fun getAllHistory(): Flow<List<HistoryChatBotEntity>>


}