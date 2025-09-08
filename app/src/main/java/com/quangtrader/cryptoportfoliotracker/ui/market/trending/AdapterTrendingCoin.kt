package com.quangtrader.cryptoportfoliotracker.ui.market.trending

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinItem
import com.quangtrader.cryptoportfoliotracker.databinding.CustomTrendingCoinsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import javax.inject.Inject

class AdapterTrendingCoin @Inject constructor() :
    BaseAdapter<CoinItem, CustomTrendingCoinsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomTrendingCoinsBinding
        get() = CustomTrendingCoinsBinding::inflate

    @SuppressLint("DefaultLocale")
    override fun bindItem(
        item: CoinItem,
        binding: CustomTrendingCoinsBinding,
        position: Int
    ) {
        binding.apply {
            Glide.with(root).load(item.thumb).into(iconTokenTrending)
            tokenTrending.text = item.name
            textMarketRank.text = item.marketCapRank.toString()
            textSymbolToken.text = item.symbol
            percentRealtime.text = item.score.toString()

            if (item.data?.priceChangePercentage24h?.get("btc") == null) {
                cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.gray))
                return
            }
            val percentText = String.format("%.2f%%", item.data.priceChangePercentage24h["btc"])
            val colorRes = item.data.priceChangePercentage24h["btc"].let {
                if (it != null) {
                    if (it >= 0) {
                        cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.green))
                        imgUpDown.setImageResource(R.drawable.up_trend)
                    } else {
                        cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.red))
                        imgUpDown.setImageResource(R.drawable.down_trend)
                    }

                }
            }
            percentRealtime.text = item.data.priceChangePercentage24h["btc"].formatPercent()
        }
    }


    fun Double?.formatPercent(digits: Int = 2): String {
        if (this == null) return "0.00%"
        return String.format("%.${digits}f%%", this)
    }
}