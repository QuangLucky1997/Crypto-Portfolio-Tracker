package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.dao.CoinService
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinEntity
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.data.roommodel.PortfolioEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManagerDBRoomRepository @Inject constructor(private val coinService: CoinService) {
    val allCoinFAV: Flow<List<CoinFav>> = coinService.getAllCoinFAV()
    val getAllFavCoin: Flow<List<CoinFav>> = coinService.getAllFavCoin()
    val getAllPortfolios: Flow<List<PortfolioEntity>> = coinService.getAllPortfolios()

//    fun getCoinsByPortfolio(portfolioId: Long): Flow<List<CoinEntity>> {
//        return coinService.getCoinsByPortfolio(portfolioId)
//    }

    fun addFav(coinFav: CoinFav): Long {
        return coinService.createFavCoin(coinFav)
    }

    fun checkIfExists(data: String): Flow<Int> {
        return coinService.checkIfExists(data)
    }

    fun updateFAVCoin(nameCoin: String) {
        coinService.deleteCoin(nameCoin)

    }

    fun deletePortfolio(idPortfolio: Long) {
        coinService.deletePortfolio(idPortfolio)
    }

    fun addPortfolio(portfolio: PortfolioEntity): Long {
        return coinService.insertPortfolio(portfolio)
    }


//    suspend fun upsertCoin(coin: CoinEntity) {
//        coinService.upsertCoin(coin)
//    }
//
//
//    suspend fun deleteCoin(id: Long) {
//        coinService.deleteCoin(id)
//    }
//
//    suspend fun getTotalPortfolioValue(portfolioId: Long): Double {
//        return coinService.getTotalPortfolioValue(portfolioId) ?: 0.0
//    }


}