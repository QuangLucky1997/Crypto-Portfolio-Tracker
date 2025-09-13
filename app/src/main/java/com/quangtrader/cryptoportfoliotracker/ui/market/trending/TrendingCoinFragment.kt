package com.quangtrader.cryptoportfoliotracker.ui.market.trending

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.unit.Constraints
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentTrendingBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
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
        handleData()
    }

    private fun handleData() {
        adapterTrendingCoin.subjectTrending = { data ->
            val intentTrending = Intent(requireActivity(), DetailTokenActivity::class.java)
            intentTrending.putExtra(Constants.EXTRA_SYMBOL_COIN, data.symbol)
            intentTrending.putExtra(Constants.EXTRA_LOGO_COIN, data.thumb)
            startActivity(intentTrending)
        }
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