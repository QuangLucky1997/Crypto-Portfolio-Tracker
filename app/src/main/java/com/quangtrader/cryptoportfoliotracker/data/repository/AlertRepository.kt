package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.dao.CoinDao
import com.quangtrader.cryptoportfoliotracker.data.roommodel.AlertDirection
import com.quangtrader.cryptoportfoliotracker.data.roommodel.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlertRepository @Inject constructor(private val alertDao: CoinDao) {
//    suspend fun createAlert(symbol: String, targetPrice: Double, direction: AlertDirection) {
//        val alert = NotificationEntity(
//            symbol = symbol,
//            targetPrice = targetPrice,
//            direction = direction
//        )
//        alertDao.insertAlert(alert)
//    }
//
////    fun observeAllAlerts(): Flow<List<NotificationEntity>> {
////        return alertDao.getAllAlertsFlow()
////    }
////
////    suspend fun getActiveAlerts(): List<NotificationEntity> {
////        return alertDao.getActiveAlerts()
////    }
////
////    suspend fun getAlertsBySymbol(symbol: String): List<NotificationEntity> {
////        return alertDao.getAlertsBySymbol(symbol)
////    }
//
//    suspend fun getAllSymbols(): List<String> {
//        return alertDao.getAllSymbols()
//    }
//
//    suspend fun markAlertTriggered(id: Int) {
//        alertDao.markTriggered(id)
//    }
//
//    suspend fun deleteAlert(alert: NotificationEntity) {
//        alertDao.deleteAlert(alert)
//    }
}