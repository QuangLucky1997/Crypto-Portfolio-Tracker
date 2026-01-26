package com.quangtrader.cryptoportfoliotracker.inject

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.ads.AdType
import com.quangtrader.cryptoportfoliotracker.common.ads.BannerAdManager
import com.quangtrader.cryptoportfoliotracker.common.ads.FullAdManager
import com.quangtrader.cryptoportfoliotracker.common.ads.OpenAdManager
import com.quangtrader.cryptoportfoliotracker.data.local.ThemeMode
import com.quangtrader.cryptoportfoliotracker.data.repository.ThemeRepository
import com.quangtrader.cryptoportfoliotracker.helper.Preferences
import com.quangtrader.cryptoportfoliotracker.common.utils.ThemeManager
import com.quangtrader.cryptoportfoliotracker.ui.market.coin.DialogLoadingAds

import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

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

    @Inject
    lateinit var prefs: Preferences
    private lateinit var firebaseAnalytics: FirebaseAnalytics



    // ads


    var lastedAdTypeShown = AdType.None
    var lastedAdLastShownTime = 0L
    var canOpenBackground = true

    var isShowAllAds = true
    var isOpenSplash = true
    var isBannerHome = true
    var isBannerShowNews = true

    var isFullScreenChanges = true
    var isFullSplash = true
    var isOpenBackground = true
    var isFullOrOpenSplash = "0"
    var openDisplayInterval = 0L
    var fullAndOpenDisplayInterval = 0L
    var fullDisplayInterval = 0L
    var fullScreenChangesInterval = 0L
    var loadingFullDisplayInterval = 1000L
    var delayFullDisplayInterval = 500L

    private var countClickAds = 0L
    private var maxClickAds = 5L

    val dialogLoadingAds = DialogLoadingAds()


    val fullSplash by lazy {
        FullAdManager(
            getString(R.string.key_full_splash),
            getString(R.string.key_full_splash_backup),
            fullDisplayInterval,
            fullAndOpenDisplayInterval
        )
    }
    val openSplash by lazy {
        OpenAdManager(
            getString(R.string.key_open_screen_splash),
            getString(R.string.key_open_screen_splash_backup),
            openDisplayInterval,
            fullAndOpenDisplayInterval
        )
    }
    val fullScreenChanges by lazy {
        FullAdManager(
            getString(R.string.key_full_screen_changes),
            getString(R.string.key_full_screen_changes_backup),
            fullScreenChangesInterval,
            fullAndOpenDisplayInterval
        )
    }

    val bannerHome by lazy { BannerAdManager(getString(R.string.key_banner_merge)) }
    val bannerDetailShowNew by lazy { BannerAdManager(getString(R.string.key_banner_detail)) }
    lateinit var themeRepository: ThemeRepository
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        // RemoteConfigManager.init()
        app = this
        createNotificationChannels()
        FirebaseApp.initializeApp(this)
        Firebase.messaging.isAutoInitEnabled = true
        val mode = ThemeMode.valueOf(
            prefs.themeMode.get()
        )
        ThemeManager.applyTheme(mode)
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


    fun isMaxClickAdsTotal(): Boolean {
        return countClickAds >= maxClickAds
    }

}