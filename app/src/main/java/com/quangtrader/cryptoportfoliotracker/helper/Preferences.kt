package com.quangtrader.cryptoportfoliotracker.helper

import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.quangtrader.cryptoportfoliotracker.data.local.ThemeMode
import javax.inject.Inject

class Preferences  @Inject constructor(private val rxPref: RxSharedPreferences) {
    val isUpgraded = rxPref.getBoolean("isUpgraded", false)
    val keyAppLanguage = rxPref.getString("keyAppLanguage", "en")
    val isConfigLanguage = rxPref.getBoolean("isConfigLanguage", false)
    val isFirstInstall = rxPref.getBoolean("isFirstInstall", false)


    val themeMode = rxPref.getString("themeMode", ThemeMode.DARK.toString())



}
