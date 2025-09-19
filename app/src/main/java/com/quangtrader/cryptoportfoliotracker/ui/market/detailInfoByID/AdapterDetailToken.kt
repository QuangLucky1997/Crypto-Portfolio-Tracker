package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.alert.AlertFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.market.MarketFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.newsInfo.NewsFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.overview.OverviewFragment


class AdapterDetailToken (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment()
            1 -> MarketFragment()
            2 -> AlertFragment()
            3-> NewsFragment()
            else -> OverviewFragment()
        }
    }
}