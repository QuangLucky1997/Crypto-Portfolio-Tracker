package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.dao.CoinDao
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.data.roommodel.TokenTop100
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManagerDBRoomRepository @Inject constructor(private val coinDao: CoinDao) {
    val allCoinFAV: Flow<List<CoinFav>> = coinDao.getAllCoinFAV()
    val getAllFavCoin: Flow<List<CoinFav>> = coinDao.getAllFavCoin()

    val allTokenTop100: Flow<List<TokenTop100>> = coinDao.getAllTop100()

    fun addFav(coinFav: CoinFav): Long {
        return coinDao.createFavCoin(coinFav)
    }

    fun checkIfExists(data: String): Flow<Int> {
        return coinDao.checkIfExists(data)
    }

    fun updateFAVCoin(nameCoin: String) {
        coinDao.deleteCoin(nameCoin)

    }

    fun saveTop100(tokenTop100: TokenTop100): Long {
        return coinDao.saveTop100Coin(tokenTop100)
    }


}