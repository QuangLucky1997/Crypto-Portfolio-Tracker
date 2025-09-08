package com.quangtrader.cryptoportfoliotracker.ui.market.trending

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentTrendingBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrendingCoinFragment : BaseFragment<FragmentTrendingBinding>() {
    private val trendingCoinViewModel: TrendingCoinViewModel by viewModels()

    @Inject
    lateinit var adapterTrendingCoin: AdapterTrendingCoin
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTrendingBinding
        get() = FragmentTrendingBinding::inflate

    override fun onViewCreated() {
        trendingCoinViewModel.loadAllTrendingCoin()
        loadData()
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            trendingCoinViewModel.tokensTrending.collect { coinData ->
                coinData.forEach {
                    adapterTrendingCoin.data.add(it.item)
                }
                binding.rvTrendingCoin.adapter = adapterTrendingCoin
            }
        }
    }
}