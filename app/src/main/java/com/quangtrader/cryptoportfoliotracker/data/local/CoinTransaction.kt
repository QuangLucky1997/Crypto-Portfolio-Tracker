package com.quangtrader.cryptoportfoliotracker.data.local

sealed class CoinTransaction {
    data class Header(val title: String) : CoinTransaction()
    data class CoinItem(
        val name: String?,
        val symbol: String?,
        val logo: String? = null,
        val source: Source
    ) : CoinTransaction()

    enum class Source { API, ROOM }
}