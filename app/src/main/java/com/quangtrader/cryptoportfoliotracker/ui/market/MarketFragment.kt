package com.quangtrader.cryptoportfoliotracker.ui.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentMarketBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.home.HomeViewModel
import com.quangtrader.cryptoportfoliotracker.utils.formatMarketCap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.getValue

@AndroidEntryPoint
class MarketFragment : BaseFragment<FragmentMarketBinding>() {

    private val adapterMarket by lazy { AdapterMarket(requireActivity()) }
    private val homeViewModel by viewModels<HomeViewModel>()
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMarketBinding
        get() = FragmentMarketBinding::inflate

    override fun onViewCreated() {
        initView()
        homeViewModel.getMarketCap()
        setData()
    }

    private fun setData() {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                homeViewModel.marketCap.collect { data ->
                    val usdCap = data?.data?.totalMarketCap["usd"] ?: 0.0
                    textMarketCap.text = formatMarketCap(usdCap)
                    val df = DecimalFormat("#,##0.00")
                    val btcDominance = data?.data?.marketCapPercentage["btc"] ?: 0.0
                    textBTCDominance.text = df.format(btcDominance) + "%"

                    val totalVol24h = data?.data?.totalVolume["usd"] ?: 0.0
                    textVol.text = formatMarketCap(totalVol24h)


                }
            }
        }

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
                    tab.text = getString(R.string.watchLists)
                }

                3 -> {
                    tab.text = getString(R.string.gainers)
                }

                4 -> {
                    tab.text = getString(R.string.losers)
                }

                5 -> {
                    tab.text = getString(R.string.categories)
                }
            }
        }.attach()
    }
}