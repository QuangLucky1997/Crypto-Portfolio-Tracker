package com.quangtrader.cryptoportfoliotracker.utils

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

