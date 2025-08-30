package com.quangtrader.cryptoportfoliotracker.ui.market.coin

import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.data.remote.Data
import com.quangtrader.cryptoportfoliotracker.databinding.CustomListTokenRealtimeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import javax.inject.Inject

class AdapterCoin @Inject constructor() : BaseAdapter<Data, CustomListTokenRealtimeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomListTokenRealtimeBinding
        get() = CustomListTokenRealtimeBinding::inflate

    override fun bindItem(
        item: Data,
        binding: CustomListTokenRealtimeBinding,
        position: Int
    ) {
        binding.apply {
            nameToken.text = item.symbol.toString()
            marketCapitalizationToken.text = item.quote?.USD?.marketCap.toString()
            priceToken.text = item.quote?.USD?.price.toString()
            percentRealtime.text = item.quote?.USD?.percentChange24h.toString()
        }
    }
}