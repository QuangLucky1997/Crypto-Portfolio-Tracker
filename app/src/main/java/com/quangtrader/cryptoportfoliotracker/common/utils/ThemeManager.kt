package com.quangtrader.cryptoportfoliotracker.common.utils

import androidx.appcompat.app.AppCompatDelegate
import com.quangtrader.cryptoportfoliotracker.data.local.ThemeMode

object ThemeManager {

    fun applyTheme(mode: ThemeMode) {
        val nightMode = when (mode) {
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
