package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.local.ThemeMode
import com.quangtrader.cryptoportfoliotracker.helper.Preferences
import javax.inject.Inject

class ThemeRepository @Inject constructor(private val prefs : Preferences) {
    fun loadSavedTheme(): ThemeMode {
        val value = prefs.themeMode.get()
        return ThemeMode.valueOf(value)
    }

//    fun saveTheme(mode: ThemeMode) {
//        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
//            .edit()
//            .putString("theme_mode", mode.name)
//            .apply()
//    }
}