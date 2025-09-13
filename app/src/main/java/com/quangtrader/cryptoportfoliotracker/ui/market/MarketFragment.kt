package com.quangtrader.cryptoportfoliotracker.ui.market

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentMarketBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketFragment : BaseFragment<FragmentMarketBinding>() {

    private val adapterMarket by lazy { AdapterMarket(requireActivity()) }
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMarketBinding
        get() = FragmentMarketBinding::inflate

    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        binding.viewPaperMarketParent.adapter = adapterMarket
        binding.viewPaperMarketParent.setUserInputEnabled(false)
        binding.tabMarket.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        TabLayoutMediator(binding.tabMarket, binding.viewPaperMarketParent) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.coin)
                }

                1 -> {
                    tab.text = getString(R.string.trending)
                }
                2 -> {
                    tab.text = getString(R.string.gainers)
                }
                3-> {
                    tab.text = getString(R.string.losers)
                }
                4 -> {
                    tab.text = getString(R.string.categories)
                }
            }
        }.attach()
    }
}