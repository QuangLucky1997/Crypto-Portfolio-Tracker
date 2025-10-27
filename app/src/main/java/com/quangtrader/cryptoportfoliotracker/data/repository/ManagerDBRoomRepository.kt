package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.dao.CoinService
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManagerDBRoomRepository @Inject constructor(private val coinService: CoinService) {
    val allCoinFAV: Flow<List<CoinFav>> = coinService.getAllCoinFAV()
    val getAllFavCoin: Flow<List<CoinFav>> = coinService.getAllFavCoin()


    fun addFav(coinFav: CoinFav): Long {
        return coinService.createFavCoin(coinFav)
    }

     fun checkIfExists(data: String): Flow<Int> {
        return coinService.checkIfExists(data)
    }

    fun updateFAVCoin(nameCoin: String, isFAV: Boolean) {
        coinService.updateFavCoin(nameCoin, isFAV)

    }




}