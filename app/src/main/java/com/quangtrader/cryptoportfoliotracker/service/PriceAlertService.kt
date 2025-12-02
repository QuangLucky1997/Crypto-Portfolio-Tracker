package com.quangtrader.cryptoportfoliotracker.service

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.repository.AlertRepository
import com.quangtrader.cryptoportfoliotracker.data.repository.BinanceWebSocketManager
import com.quangtrader.cryptoportfoliotracker.data.roommodel.AlertDirection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class PriceAlertService : LifecycleService() {
//    @Inject lateinit var repository: AlertRepository
//    @Inject lateinit var ws: BinanceWebSocketManager
//
//    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//
//    override fun onCreate() {
//        super.onCreate()
//        startForegroundService()
//        startAlertChecking()
//    }
//
//    private fun startForegroundService() {
//        val notification = NotificationCompat.Builder(this, "alert_channel")
//            .setContentTitle("Crypto Price Alerts")
//            .setContentText("Đang theo dõi giá realtime…")
//            .setSmallIcon(R.drawable.notification)
//            .build()
//
//        startForeground(1001, notification)
//    }
//
//    private fun startAlertChecking() {
//        scope.launch {
//            val symbols = repository.getAllSymbols()
//
//            if (symbols.isEmpty()) {
//                stopSelf()
//                return@launch
//            }
//
//            ws.connect(symbols) { text ->
//                processWebSocketMessage(text)
//            }
//        }
//    }
//
//    private fun processWebSocketMessage(json: String) {
//        scope.launch {
//            try {
//                val streamObj = JSONObject(json)
//                val data = streamObj.getJSONObject("data")
//
//                val symbol = data.getString("s")
//                val price = data.getString("c").toDouble()
//
//               // val alerts = repository.getAlertsBySymbol(symbol)
//
//                for (alert in alerts) {
//                    when (alert.direction) {
//                        AlertDirection.ABOVE -> {
//                            if (price >= alert.targetPrice) {
//                                if (hasNotificationPermission()) {
//                                    sendNotificationSafe(symbol, price)
//                                } else {
//                                    Log.w(TAG, "Missing POST_NOTIFICATIONS permission — notification skipped for alert id=${alert.id}")
//                                }
//                                repository.markAlertTriggered(alert.id)
//                            }
//                        }
//                        AlertDirection.BELOW -> {
//                            if (price <= alert.targetPrice) {
//                                if (hasNotificationPermission()) {
//                                    sendNotificationSafe(symbol, price)
//                                } else {
//                                    Log.w(TAG, "Missing POST_NOTIFICATIONS permission — notification skipped for alert id=${alert.id}")
//                                }
//                                repository.markAlertTriggered(alert.id)
//                            }
//                        }
//                    }
//                }
//
//            } catch (e: Exception) {
//                Log.e(TAG, "processWebSocketMessage error", e)
//            }
//        }
//    }
//
//    private fun hasNotificationPermission(): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        } else {
//            true
//        }
//    }
//
//    /**
//     * Wrapper that actually posts the notification.
//     * If Lint still complains, we suppress MissingPermission here because we check permission before calling.
//     */
//    @SuppressLint("MissingPermission")
//    private fun sendNotificationSafe(symbol: String, price: Double) {
//        val notification = NotificationCompat.Builder(this, "alert_channel")
//            .setSmallIcon(R.drawable.notification)
//            .setContentTitle("Alert: $symbol")
//            .setContentText("Giá hiện tại: $price")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setAutoCancel(true)
//            .build()
//
//        NotificationManagerCompat.from(this)
//            .notify(System.currentTimeMillis().toInt(), notification)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        ws.close()
//        scope.cancel()
//    }
//
//    companion object {
//        private const val TAG = "PriceAlertService"
//    }
}