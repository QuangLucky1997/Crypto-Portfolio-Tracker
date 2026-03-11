package com.quangtrader.cryptoportfoliotracker.ui.market.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.formatMarketCap2
import com.quangtrader.cryptoportfoliotracker.common.utils.formatPrice
import com.quangtrader.cryptoportfoliotracker.data.remote.TradFi
import com.quangtrader.cryptoportfoliotracker.data.remote.TradFiClean
import com.quangtrader.cryptoportfoliotracker.databinding.CustomCategoriesCoinBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import javax.inject.Inject

class AdapterTradFi @Inject constructor() :
    BaseAdapter<TradFiClean, CustomCategoriesCoinBinding>(
        DiffCallbackTradFi()
    ) {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomCategoriesCoinBinding
        get() = CustomCategoriesCoinBinding::inflate

    override fun bindItem(
        item: TradFiClean,
        binding: CustomCategoriesCoinBinding,
        position: Int
    ) {
        binding.apply {
            tokenTradFi.text = item.displayName
            textVol.text = item.volume.formatMarketCap2() ?: "--"
            priceToken.text = "$".plus(item.lastPrice.formatPrice())
            val colorRes = item.priceChangePercent
            if (colorRes >= 0) {
                cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.green))
                imgUpDown.setImageResource(R.drawable.up_trend)
            } else {
                cardPercent24H.setCardBackgroundColor(root.resources.getColor(R.color.red))
                imgUpDown.setImageResource(R.drawable.down_trend)
            }
            percentRealtime.text = item.priceChangePercent.toString().plus("%")
        }

    }



    class DiffCallbackTradFi : DiffUtil.ItemCallback<TradFiClean>() {
        override fun areItemsTheSame(
            oldItem: TradFiClean,
            newItem: TradFiClean
        ): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(
            oldItem: TradFiClean,
            newItem: TradFiClean
        ): Boolean {
            return oldItem == newItem
        }
    }

}