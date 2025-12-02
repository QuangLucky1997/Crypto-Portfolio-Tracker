package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.alert

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterAlertToken (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AlertByPriceFragment()
            1 -> AlertByPercentFragment()
            else -> AlertByPriceFragment()
        }
    }
}