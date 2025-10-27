package com.quangtrader.cryptoportfoliotracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinService {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createFavCoin(coin: CoinFav): Long

    @Query("SELECT * FROM CoinFav")
    fun getAllCoinFAV(): Flow<List<CoinFav>>

    @Query("SELECT COUNT(*) FROM CoinFav WHERE name = :name")
    fun checkIfExists(name: String): Flow<Int>

    @Query("UPDATE CoinFav SET isFAV = :isFAV WHERE symbol = :symbolData")
    fun updateFavCoin(symbolData: String, isFAV: Boolean)


    @Query("SELECT * FROM CoinFav WHERE isFAV = 1")
    fun getAllFavCoin(): Flow<List<CoinFav>>



}