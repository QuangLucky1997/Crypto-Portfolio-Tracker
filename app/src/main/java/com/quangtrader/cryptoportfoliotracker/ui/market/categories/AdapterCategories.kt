package com.quangtrader.cryptoportfoliotracker.ui.market.categories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.remote.ResponseCategoryCoin
import com.quangtrader.cryptoportfoliotracker.databinding.CustomCategoriesCoinBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import javax.inject.Inject

class AdapterCategories @Inject constructor() : BaseAdapter<ResponseCategoryCoin, CustomCategoriesCoinBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomCategoriesCoinBinding
        get() = CustomCategoriesCoinBinding::inflate

    override fun bindItem(
        item: ResponseCategoryCoin,
        binding: CustomCategoriesCoinBinding,
        position: Int
    ) {
        binding.apply {
            Glide.with(root)
                .load(item.top3Coins?.getOrNull(0))
                .placeholder(R.drawable.coin)
                .error(R.drawable.coin)
                .into(iconToken1)

            Glide.with(root)
                .load(item.top3Coins?.getOrNull(1))
                .placeholder(R.drawable.coin)
                .error(R.drawable.coin)
                .into(iconToken2)

            Glide.with(root)
                .load(item.top3Coins?.getOrNull(2))
                .placeholder(R.drawable.coin)
                .error(R.drawable.coin)
                .into(iconToken3)
            nameCategories.text = item.name
            marketCapCategories.text = item.marketCap?.formatMarketCap()
            val colorRes = item.marketCapChange24h
            if (colorRes != null) {
                if (colorRes >= 0) {
                    cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.green))
                    imgUpDown.setImageResource(R.drawable.up_trend)
                } else {
                    cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.red))
                    imgUpDown.setImageResource(R.drawable.down_trend)
                }

            }
            percentRealtime.text = item.marketCapChange24h.formatPercent()
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