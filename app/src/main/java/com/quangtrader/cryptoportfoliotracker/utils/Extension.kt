package com.quangtrader.cryptoportfoliotracker.utils

fun Double?.formatPrice(digits: Int = 2): String {
    if (this == null) return "0.00"
    return "%.${digits}f".format(this)
}

