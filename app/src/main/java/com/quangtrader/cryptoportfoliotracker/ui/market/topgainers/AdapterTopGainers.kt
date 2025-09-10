package com.quangtrader.cryptoportfoliotracker.ui.market.topgainers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.remote.GainerOrLoserCoinGeckoResponse
import com.quangtrader.cryptoportfoliotracker.databinding.CustomGainerLoserCoinsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import java.text.DecimalFormat
import javax.inject.Inject

class AdapterTopGainers @Inject constructor() : BaseAdapter<GainerOrLoserCoinGeckoResponse, CustomGainerLoserCoinsBinding>(){
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomGainerLoserCoinsBinding
        get() = CustomGainerLoserCoinsBinding ::inflate

    @SuppressLint("DefaultLocale")
    override fun bindItem(
        item: GainerOrLoserCoinGeckoResponse,
        binding: CustomGainerLoserCoinsBinding,
        position: Int
    ) {
        binding.apply {
            Glide.with(root).load(item.image).into(iconTokenTrending)
            tokenGainers.text = item.name
            textMarketRank.text = item.marketCapRank.toString()
            textSymbolToken.text = item.symbol
            val df = DecimalFormat("#.##")
            df.minimumFractionDigits = 2
            df.maximumFractionDigits = 2
            val formatted = df.format(item.currentPrice)
            priceToken.text = "$".plus(formatted.toString())
            //val percentText = String.format("%.2f%%", item.priceChangePercentage24h)
            val colorRes = item.priceChangePercentage24h
            if (colorRes != null) {
                if (colorRes >= 0) {
                    cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.green))
                    imgUpDown.setImageResource(R.drawable.up_trend)
                } else {
                    cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.red))
                    imgUpDown.setImageResource(R.drawable.down_trend)
                }

            }
            percentRealtime.text = item.priceChangePercentage24h.formatPercent()
        }
    }

    fun Double?.formatPercent(digits: Int = 2): String {
        if (this == null) return "0.00%"
        return String.format("%.${digits}f%%", this)
    }
}