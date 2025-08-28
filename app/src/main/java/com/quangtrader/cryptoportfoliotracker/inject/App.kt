package com.quangtrader.cryptoportfoliotracker.inject
import android.app.Application

import com.google.firebase.analytics.FirebaseAnalytics

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var app: App
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
//        FirebaseApp.initializeApp(this)
//        Firebase.messaging.isAutoInitEnabled = true
    }

}