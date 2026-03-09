package com.quangtrader.cryptoportfoliotracker.ui.market.coin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.formatMarketCap2
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
import com.quangtrader.cryptoportfoliotracker.databinding.CustomListTokenRealtimeBinding
import com.quangtrader.cryptoportfoliotracker.common.utils.formatPercent
import com.quangtrader.cryptoportfoliotracker.common.utils.formatPrice
import javax.inject.Inject


class AdapterCoin @Inject constructor() :
    ListAdapter<CoinUI, AdapterCoin.CoinViewHolder>(DiffCallback) {

    var subjectDetail: ((CoinUI) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = CustomListTokenRealtimeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CoinViewHolder(
        private val binding: CustomListTokenRealtimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CoinUI) = with(binding) {
            if (iconToken.tag != item.logo) {
                iconToken.tag = item.logo
                Glide.with(root).load(item.logo).into(iconToken)
            }
            nameToken.text = item.symbol
            marketCapitalizationToken.text = item.marketCap?.formatMarketCap2() ?: "--"

            priceToken.text = formatPrice(item.price)

            // Percent
            percentRealtime.text = item.percentChange24h.formatPercent()
            val color = if ((item.percentChange24h ?: 0.0) >= 0)
                R.color.green else R.color.red
            cardPercent24H.setCardBackgroundColor(root.resources.getColor(color))

            viewToken.setOnClickListener {
                subjectDetail?.invoke(item)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CoinUI>() {

        override fun areItemsTheSame(oldItem: CoinUI, newItem: CoinUI): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CoinUI, newItem: CoinUI): Boolean =
            oldItem.price == newItem.price &&
                    oldItem.percentChange24h == newItem.percentChange24h
    }



}
