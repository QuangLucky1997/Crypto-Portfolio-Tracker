package com.quangtrader.cryptoportfoliotracker.ui.market.trending

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinItem
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.databinding.CustomTrendingCoinsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import java.text.DecimalFormat
import javax.inject.Inject

class AdapterTrendingCoin @Inject constructor() :
    BaseAdapter<CoinItem, CustomTrendingCoinsBinding>(DiffCallbackNewsTrending()) {

    var subjectTrending: ((CoinItem) -> Unit)? = null
    private val df = DecimalFormat("#.##").apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

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

            val price = item.data?.price ?: 0.0
            priceToken.text = "$${df.format(price)}"
            val btcChange = item.data?.priceChangePercentage24h?.get("btc")
            if (btcChange == null) {
                cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.gray, null))
                percentRealtime.text = "--"
                imgUpDown.setImageResource(0)
            } else {
                percentRealtime.text = btcChange.formatPercent()

                if (btcChange >= 0) {
                    cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.green, null))
                    imgUpDown.setImageResource(R.drawable.up_trend)
                } else {
                    cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.red, null))
                    imgUpDown.setImageResource(R.drawable.down_trend)
                }
            }
            viewTokenTrending.clicks {
                subjectTrending?.invoke(item)
            }
        }
    }

    private fun Double?.formatPercent(digits: Int = 2): String {
        if (this == null) return "0.00%"
        return String.format("%.${digits}f%%", this)
    }

    class DiffCallbackNewsTrending : DiffUtil.ItemCallback<CoinItem>() {
        override fun areItemsTheSame(oldItem: CoinItem, newItem: CoinItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CoinItem, newItem: CoinItem): Boolean {
            return oldItem == newItem
        }
    }
}