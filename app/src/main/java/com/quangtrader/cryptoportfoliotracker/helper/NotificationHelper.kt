package com.quangtrader.cryptoportfoliotracker.helper

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.roommodel.NotificationEntity
import com.quangtrader.cryptoportfoliotracker.inject.App
import com.quangtrader.cryptoportfoliotracker.ui.home.HomeActivity

class NotificationHelper(private val context: Context) {
    fun createServiceNotification(text: String): Notification {
        val intent = Intent(context, HomeActivity::class.java)
        val pi = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, App.CHANNEL_SERVICE)
            .setContentTitle("Price Alert Service")
            .setContentText(text)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showAlertNotification(alert: NotificationEntity, currentPrice: Double) {
        val intent = Intent(context, HomeActivity::class.java)
        val pi = PendingIntent.getActivity(
            context,
            alert.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, App.CHANNEL_ALERT)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("${alert.symbol} reached ${alert.targetPrice}")
            .setContentText("Current: $currentPrice")
            .setContentIntent(pi)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(context).notify(alert.id, builder.build())
    }
}