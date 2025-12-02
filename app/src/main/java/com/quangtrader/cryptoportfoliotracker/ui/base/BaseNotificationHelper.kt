package com.quangtrader.cryptoportfoliotracker.ui.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat

class BaseNotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "price_alert_channel"
        const val CHANNEL_NAME = "Price Alerts"
        const val CHANNEL_DESC = "Notify user when crypto price reaches target"
    }

    init {
        createNotificationChannel()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
            }
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
        } else true
    }
    fun requestNotificationPermission(activity: ComponentActivity, onResult: (Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val requestPermissionLauncher =
                activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                    onResult(granted)
                }
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Android < 13 luôn grant
            onResult(true)
        }
    }
    fun isNotificationEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}