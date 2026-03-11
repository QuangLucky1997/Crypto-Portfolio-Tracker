package com.quangtrader.cryptoportfoliotracker.common.utils

import android.app.Activity
import android.app.Fragment
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
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
            .setScale(decimals, RoundingMode.DOWN)
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
    val suffix = listOf("", "K", "M", "B", "T")
    if (number < 1000) {
        return DecimalFormat("#,##0").format(number)
    }
    val tier = (Math.log10(number) / 3).toInt()
    if (tier >= suffix.size) {
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
            Locale.US
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
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun formatPriceCustom(price: Double?): String {
    if (price == null) return "$0.00"
    return when {
        price >= 1.0 -> {
            val df = DecimalFormat("#,##0.00")
            df.format(price)
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
            df.format(price)
        }

        else -> {
            val df = DecimalFormat("0.000000")
            df.format(price)
        }
    }
}

fun EditText.textChanges(): Flow<String> = callbackFlow {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            trySend(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    addTextChangedListener(watcher)
    send(text.toString())
    awaitClose { removeTextChangedListener(watcher) }
}

fun String.parseToBigDecimal(): BigDecimal? {
    val cleanThousands = this.replace(".", "")
    val finalFormat = cleanThousands.replace(",", ".")
    return finalFormat.toBigDecimalOrNull()
}

fun formatCurrency(value: BigDecimal): String {
    return "$${value.setScale(2, RoundingMode.HALF_UP).toPlainString()}"
}

fun formatPercentage(value: BigDecimal): String {
    return "${value.setScale(2, RoundingMode.HALF_UP).toPlainString()}%"
}

fun Long.toDateTimeString(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

fun sendEmailViaGmail(context: Context, to: String, subject: String, body: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
        setPackage("com.google.android.gm")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        try {
            intent.setPackage(null)
            context.startActivity(Intent.createChooser(intent, "Send mail..."))
        } catch (ex: Exception) {
            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }
}

fun openPlayStore(context: Context) {
    val uri = Uri.parse("market://details?id=${context.packageName}")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
            )
        )
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}


fun getAppVersion(context: Context): Pair<String, Long> {
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        @Suppress("DEPRECATION")
        context.packageManager.getPackageInfo(context.packageName, 0)
    }

    val versionName = packageInfo.versionName ?: "Unknown"
    val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        @Suppress("DEPRECATION")
        packageInfo.versionCode.toLong()
    }

    return Pair(versionName, versionCode)
}


fun Double.formatMarketCap2(): String {
    return when {
        this >= 1_000_000_000_000 -> String.format("%.2fT", this / 1_000_000_000_000)
        this >= 1_000_000_000 -> String.format("%.2fB", this / 1_000_000_000)
        this >= 1_000_000 -> String.format("%.2fM", this / 1_000_000)
        this >= 1_000 -> String.format("%.2fK", this / 1_000)
        else -> String.format("%.2f", this)
    }
}







