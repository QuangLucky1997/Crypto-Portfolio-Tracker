package com.quangtrader.cryptoportfoliotracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinEntity
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.data.roommodel.PortfolioEntity
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

    @Query("DELETE FROM CoinFav WHERE symbol = :symbolData")
    fun deleteCoin(symbolData: String): Int


    @Query("SELECT * FROM CoinFav WHERE isFAV = 1")
    fun getAllFavCoin(): Flow<List<CoinFav>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPortfolio(portfolio: PortfolioEntity): Long

    @Query("SELECT * FROM portfolio ORDER BY createdAt DESC")
    fun getAllPortfolios(): Flow<List<PortfolioEntity>>

    @Query("DELETE FROM portfolio WHERE id = :portfolioId")
    fun deletePortfolio(portfolioId: Long)


//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsertCoin(coin: CoinEntity)
//
//    @Query("SELECT * FROM CoinEntity WHERE portfolioId = :portfolioId ORDER BY name ASC")
//    fun getCoinsByPortfolio(portfolioId: Long): Flow<List<CoinEntity>>

//    @Query("DELETE FROM CoinEntity WHERE id = :coinId")
//    suspend fun deleteCoin(coinId: Long)

//    @Query(
//        """
//    SELECT COALESCE(SUM(quantity * currentPrice), 0.0)
//    FROM CoinEntity
//    WHERE portfolioId = :portfolioId
//"""
//    )
//    fun getTotalPortfolioValue(portfolioId: Long): Double


}