package com.quangtrader.cryptoportfoliotracker.ui.market

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.quangtrader.cryptoportfoliotracker.ui.market.coin.CoinFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.exchange.ExchangeFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.trending.TrendingCoinFragment

class AdapterMarket(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CoinFragment()
            1 -> TrendingCoinFragment()
            2 -> ExchangeFragment()
            else -> CoinFragment()
        }
    }
}