package com.quangtrader.cryptoportfoliotracker.ui.market.topgainers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.common.utils.formatMarketCap2
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
import com.quangtrader.cryptoportfoliotracker.databinding.CustomGainerLoserCoinsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import java.text.DecimalFormat
import javax.inject.Inject

@Suppress("DEPRECATION")
class AdapterTopGainers @Inject constructor() :
    BaseAdapter<CoinUI, CustomGainerLoserCoinsBinding>(
        DiffCallbackTopGainers()
    ) {

    var subjectGainers: ((CoinUI) -> Unit)? = null
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomGainerLoserCoinsBinding
        get() = CustomGainerLoserCoinsBinding::inflate

    @SuppressLint("DefaultLocale")
    override fun bindItem(
        item: CoinUI,
        binding: CustomGainerLoserCoinsBinding,
        position: Int
    ) {
        binding.apply {
            Glide.with(root).load(item.logo).into(iconTokenTrending)
            tokenGainers.text = item.symbol
            textMarketRank.text = item.marketCap?.formatMarketCap2() ?: "--"
            textSymbolToken.text = item.symbol
            val df = DecimalFormat("#.##")
            df.minimumFractionDigits = 2
            df.maximumFractionDigits = 2
            val formatted = df.format(item.price)
            priceToken.text = "$".plus(formatted.toString())
            val colorRes = item.percentChange24h
            if (colorRes != null) {
                if (colorRes >= 0) {
                    cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.green))
                    imgUpDown.setImageResource(R.drawable.up_trend)
                } else {
                    cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.red))
                    imgUpDown.setImageResource(R.drawable.down_trend)
                }

            }
            percentRealtime.text = item.percentChange24h.formatPercent()
            viewToken.clicks {
                subjectGainers?.invoke(item)
            }
        }
    }

    fun Double?.formatPercent(digits: Int = 2): String {
        if (this == null) return "0.00%"
        return String.format("%.${digits}f%%", this)
    }

    class DiffCallbackTopGainers : DiffUtil.ItemCallback<CoinUI>() {
        override fun areItemsTheSame(
            oldItem: CoinUI,
            newItem: CoinUI
        ): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(
            oldItem: CoinUI,
            newItem: CoinUI
        ): Boolean {
            return oldItem == newItem
        }
    }
}