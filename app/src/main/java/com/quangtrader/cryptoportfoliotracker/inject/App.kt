package com.quangtrader.cryptoportfoliotracker.inject
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

import com.google.firebase.analytics.FirebaseAnalytics

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var app: App
        const val CHANNEL_SERVICE = "price_service_channel"
        const val CHANNEL_ALERT = "price_alert_channel"
    }
    init {
        app = this
    }

//    @Inject
//    lateinit var prefs: Preferences
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
      //  RemoteConfigManager.init()
        app = this
        createNotificationChannels()
//        FirebaseApp.initializeApp(this)
//        Firebase.messaging.isAutoInitEnabled = true
    }


        private fun createNotificationChannels() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val serviceChannel = NotificationChannel(
                    CHANNEL_SERVICE,
                    "Price Alert Service",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Notification channel for running service."
                }
                val alertChannel = NotificationChannel(
                    CHANNEL_ALERT,
                    "Price Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications when price hits target."
                }
                val nm = getSystemService(NotificationManager::class.java)
                nm.createNotificationChannel(serviceChannel)
                nm.createNotificationChannel(alertChannel)
            }
        }



}