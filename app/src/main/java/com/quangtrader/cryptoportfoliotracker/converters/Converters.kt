package com.quangtrader.cryptoportfoliotracker.converters

import androidx.room.TypeConverter
import com.quangtrader.cryptoportfoliotracker.data.roommodel.AlertDirection

class Converters {

    @TypeConverter
    fun fromAlertDirection(direction: AlertDirection?): String? {
        return direction?.name
    }

    @TypeConverter
    fun toAlertDirection(value: String?): AlertDirection? {
        return value?.let { AlertDirection.valueOf(it) }
    }
}