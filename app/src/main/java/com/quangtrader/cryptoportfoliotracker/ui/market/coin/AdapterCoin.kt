package com.quangtrader.cryptoportfoliotracker.ui.market.coin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
import com.quangtrader.cryptoportfoliotracker.databinding.CustomListTokenRealtimeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import javax.inject.Inject
import java.text.DecimalFormat


class AdapterCoin @Inject constructor() : BaseAdapter<CoinUI, CustomListTokenRealtimeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomListTokenRealtimeBinding
        get() = CustomListTokenRealtimeBinding::inflate

    @SuppressLint("DefaultLocale")
    override fun bindItem(
        item: CoinUI,
        binding: CustomListTokenRealtimeBinding,
        position: Int
    ) {
        binding.apply {
            Glide.with(root).load(item.logo).into(iconToken);
            nameToken.text = item.symbol
            marketCapitalizationToken.text = item.marketCap?.formatMarketCap() ?: "--"
            val df = DecimalFormat("#.##")
            df.minimumFractionDigits = 2
            df.maximumFractionDigits = 2
            val formatted = df.format(item.price)
            priceToken.text = "$".plus(formatted.toString())
            if (item.percentChange24h == null) {
                cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.gray))
                return
            }
            val percentText = String.format("%.2f%%", item.percentChange24h)
            val colorRes = item.percentChange24h?.let {
                if (it >= 0) cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.green)) else cardPercent24H.setCardBackgroundColor(
                    root.resources.getColor(R.color.red)
                )
            }
            percentRealtime.text = item.percentChange24h.formatPercent()
        }
    }

    @SuppressLint("DefaultLocale")
    fun Double.formatMarketCap(): String {
        return when {
            this >= 1_000_000_000_000 -> String.format("%.2fT", this / 1_000_000_000_000)
            this >= 1_000_000_000 -> String.format("%.2fB", this / 1_000_000_000)
            this >= 1_000_000 -> String.format("%.2fM", this / 1_000_000)
            this >= 1_000 -> String.format("%.2fK", this / 1_000)
            else -> String.format("%.2f", this)
        }
    }

    fun Double?.formatPercent(digits: Int = 2): String {
        if (this == null) return "0.00%"
        return String.format("%.${digits}f%%", this)
    }


}

