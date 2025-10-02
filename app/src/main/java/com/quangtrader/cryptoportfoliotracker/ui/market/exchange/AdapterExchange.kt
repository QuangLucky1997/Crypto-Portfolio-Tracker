package com.quangtrader.cryptoportfoliotracker.ui.market.exchange

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.remote.Ticker
import com.quangtrader.cryptoportfoliotracker.data.remote.Ticket
import com.quangtrader.cryptoportfoliotracker.databinding.CustomExchangeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import com.quangtrader.cryptoportfoliotracker.utils.formatVolume
import javax.inject.Inject

class AdapterExchange @Inject constructor() :
    BaseAdapter<Ticker, CustomExchangeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomExchangeBinding
        get() = CustomExchangeBinding::inflate

    @SuppressLint("DefaultLocale")
    override fun bindItem(
        item: Ticker,
        binding: CustomExchangeBinding,
        position: Int
    ) {
        binding.apply {
            textSymbolToken.text = item.base.plus("/USDT")
            nameExchange.text = item.market?.name
            when (item.market?.name) {
                "Binance" -> iconExchange.setImageResource(R.drawable.binance)
                "KuCoin" -> iconExchange.setImageResource(R.drawable.kucoin)
                "MEXC" -> iconExchange.setImageResource(R.drawable.mexc)
                "Bitget" -> iconExchange.setImageResource(R.drawable.bitget)
                "Bybit" -> iconExchange.setImageResource(R.drawable.bybit)
                "HTX" -> iconExchange.setImageResource(R.drawable.htx)
                "OKX" -> iconExchange.setImageResource(R.drawable.okx)
                "BingX" -> iconExchange.setImageResource(R.drawable.bingx)
                "Kraken" -> iconExchange.setImageResource(R.drawable.kraken)
                "Gate" -> iconExchange.setImageResource(R.drawable.gate)
                "Upbit"->iconExchange.setImageResource(R.drawable.upbit)
                "Crypto.com Exchange"->iconExchange.setImageResource(R.drawable.crypto)
                "XT.COM"->iconExchange.setImageResource(R.drawable.xt)
                else -> {
                    iconExchange.setImageResource(R.drawable.coin)
                }
            }
            priceToken.text = "$".plus(item.last.toString())
            item.volume?.let { textVolume.text = formatVolume(it) }
        }
    }


}