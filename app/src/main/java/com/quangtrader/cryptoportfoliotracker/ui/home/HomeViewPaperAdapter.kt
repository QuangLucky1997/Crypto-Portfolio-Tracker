package com.quangtrader.cryptoportfoliotracker.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.quangtrader.cryptoportfoliotracker.ui.calculator.CalculatorFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.MarketFragment
import com.quangtrader.cryptoportfoliotracker.ui.news.NewsFragment
import com.quangtrader.cryptoportfoliotracker.ui.portfolio.PortfolioFragment
import com.quangtrader.cryptoportfoliotracker.ui.settings.SettingsFragment

class HomeViewPaperAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MarketFragment()
            1 -> CalculatorFragment()
            2 -> NewsFragment()
            3 -> SettingsFragment()
            else -> MarketFragment()
        }
    }
    }