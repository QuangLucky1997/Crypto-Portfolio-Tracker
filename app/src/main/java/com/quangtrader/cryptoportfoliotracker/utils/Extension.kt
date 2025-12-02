package com.quangtrader.cryptoportfoliotracker.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.compose.ui.text.intl.Locale
import com.quangtrader.cryptoportfoliotracker.data.remote.GlobalData
import java.text.DecimalFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun Double?.formatPrice(digits: Int = 2): String {
    if (this == null) return "0.00"
    return "%.${digits}f".format(this)
}

fun Double?.formatPriceTrending(price: Double, decimals: Int = 6): String {
    return try {
        price.toBigDecimal()
            .setScale(decimals, java.math.RoundingMode.DOWN)
            .toPlainString()
    } catch (e: Exception) {
        "0"
    }
}

fun Double?.formatPercent(digits: Int = 2): String {
    if (this == null) return "0.00%"
    return String.format("%.${digits}f%%", this)
}

fun formatVolume(number: Double): String {
    if (number < 1000) {
        return DecimalFormat("#,##0.00").format(number)
    }
    val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
    val numValue = number.toLong()
    val value = floor(log10(numValue.toDouble())).toInt()
    val base = value / 3
    if (value >= 3 && base < suffix.size) {
        val formattedNumber = DecimalFormat("#0.0").format(
            numValue / 10.0.pow((base * 3).toDouble())
        )
        return "$formattedNumber${suffix[base]}"
    } else {
        return DecimalFormat("#,##0").format(numValue)
    }
}

fun getRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "${seconds}s ago"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        else -> "${days}d ago"
    }
}

fun formatMarketCap(number: Double): String {
    val suffix = listOf("", "K", "M", "B", "T") // K: nghìn, M: triệu, B: tỷ, T: nghìn tỷ
    if (number < 1000) {
        return DecimalFormat("#,##0").format(number)
    }
    val tier = (Math.log10(number) / 3).toInt()
    if (tier >= suffix.size) {
        // Đối với các số rất lớn, chỉ cần hiển thị ở dạng nghìn tỷ (T)
        val value = number / 10.0.pow(12)
        return "${DecimalFormat("#,##0.00").format(value)}T"
    }
    val value = number / 10.0.pow((tier * 3).toDouble())
    return "${DecimalFormat("#,##0.00").format(value)}${suffix[tier]}"
}

fun showKeyboard(context: Context, editText: EditText) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun formatDateTime(isoDateTimeString: String?, outputPattern: String = "dd/MM/yyyy"): String {
    if (isoDateTimeString.isNullOrEmpty()) {
        return ""
    }
    return try {
        val zonedDateTime = ZonedDateTime.parse(isoDateTimeString)
        val outputFormatter = DateTimeFormatter.ofPattern(
            outputPattern,
            java.util.Locale.US
        )
        zonedDateTime.format(outputFormatter)
    } catch (e: Exception) {
        isoDateTimeString
    }
}

fun formatPrice(price: Double?): String {
    if (price == null) return "$0.00"
    return when {
        price >= 1.0 -> {
            val df = DecimalFormat("#,##0.00")
            "$" + df.format(price)
        }

        price < 0.000001 && price > 0 -> {
            var tempPrice = price
            var decimalPlaces = 0
            while (tempPrice < 1 && tempPrice > 0) {
                tempPrice *= 10
                decimalPlaces++
            }
            val pattern = "0." + "0".repeat(decimalPlaces + 2)
            val df = DecimalFormat(pattern)
            "$" + df.format(price)
        }

        else -> {
            val df = DecimalFormat("0.000000")
            "$" + df.format(price)
        }
    }
}


fun openAppInfo(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${context.packageName}")
    }
    context.startActivity(intent)
}



