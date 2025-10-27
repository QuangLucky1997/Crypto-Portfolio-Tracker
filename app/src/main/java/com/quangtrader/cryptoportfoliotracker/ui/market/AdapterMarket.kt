package com.quangtrader.cryptoportfoliotracker.ui.market

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.quangtrader.cryptoportfoliotracker.ui.market.categories.CategoriesFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.coin.CoinFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.topgainers.TopGainersFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.toploser.TopLosersFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.trending.TrendingCoinFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.watchlists.WatchListsFragment

class AdapterMarket(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CoinFragment()
            1 -> TrendingCoinFragment()
            2 -> WatchListsFragment()
            3 -> TopGainersFragment()
            4 -> TopLosersFragment()
            5 -> CategoriesFragment()
            else -> CoinFragment()
        }
    }
}