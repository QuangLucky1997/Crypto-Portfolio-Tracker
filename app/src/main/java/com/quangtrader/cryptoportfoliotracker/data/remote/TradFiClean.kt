package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep

@Keep
data class TradFiClean(
    val symbol: String,
    val displayName: String,
    val lastPrice: Double,
    val priceChangePercent: Double,
    val volume : Double
)

val whiteListSymbols = listOf(
    "NCSIDOWJONES2USD-USDT",
    "NCSINIKKEI2252USD-USDT",
    "NCSIS&P5002USD-USDT",
    "NCSINASDAQ1002USD-USDT",
    "NCSIDAX402USD-USDT",
    "NCSIFTSE1002USD-USDT",


    "NCCOGOLD2USD-USDT",    // Vàng
    "NCCOSILVER2USD-USDT",  // Bạc
    "NCCOWTI2USD-USDT",     // Dầu WTI
    "NCCOBRENT2USD-USDT",   // Dầu Brent
    "NCCONATGAS2USD-USDT",  // Khí thiên nhiên
    "NCCOCOPPER2USD-USDT",  // Đồng
    "NCCOALUMINIUM2USD-USDT",
    "NCCOCOFFEE2USD-USDT",
    "NCCOCOCOA2USD-USDT",
    "NCCOSOYBEANS2USD-USDT",


    "NCFXEUR2CAD-USDT",
    "NCFXGBP2USD-USDT",
    "NCFXUSD2JPY-USDT",
    "NCFXEUR2CHF-USDT",
    "NCFXEUR2JPY-USDT",
    "NCFXAUD2JPY-USDT",
    "NCFXAUD2CHF-USDT",

    "NCFXEUR2GBP-USDT",   // Euro / Bảng Anh
    "NCFXUSD2CAD-USDT",   // Đô la Mỹ / Đô la Canada
    "NCFXUSD2CHF-USDT",   // Đô la Mỹ / Franc Thụy Sĩ
    "NCFXNZD2USD-USDT",   // Đô la New Zealand / Đô la Mỹ
    "NCFXEUR2AUD-USDT",   // Euro / Đô la Úc
    "NCFXGBP2JPY-USDT",   // Bảng Anh / Yên Nhật
    "NCFXAUD2USD-USDT",   // Đô la Úc / Đô la Mỹ
    "NCFXCAD2JPY-USDT",   // Đô la Canada / Yên Nhật
    "NCFXGBP2CHF-USDT",   // Bảng Anh / Franc Thụy Sĩ
    "NCFXEUR2NZD-USDT" ,   // Euro / Đô la New Zealand

    "NCSKAAPL2USD-USDT", "NCSKAMZN2USD-USDT", "NCSKTSLA2USD-USDT", "NCSKNFLX2USD-USDT", "NCSKNVDA2USD-USDT", "NCSKMSFT2USD-USDT"
)