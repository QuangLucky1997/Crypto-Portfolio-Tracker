package com.quangtrader.cryptoportfoliotracker.ui.news

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class AdapterNews(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TrendingNewsFragment()
            1 -> BullishNewsFragment()
            2 -> BearishNewsFragment()
            3 -> LatestNewsFragment()
            else -> TrendingNewsFragment()
        }
    }
}