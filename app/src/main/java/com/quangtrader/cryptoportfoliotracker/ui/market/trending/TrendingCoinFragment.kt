package com.quangtrader.cryptoportfoliotracker.ui.market.trending

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentTrendingBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle

@AndroidEntryPoint
class TrendingCoinFragment : BaseFragment<FragmentTrendingBinding>() {
    private val trendingCoinViewModel: TrendingCoinViewModel by viewModels()

    @Inject
    lateinit var adapterTrendingCoin: AdapterTrendingCoin
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTrendingBinding
        get() = FragmentTrendingBinding::inflate

    override fun onViewCreated() {
        trendingCoinViewModel.loadAllTrendingCoin()
        binding.rvTrendingCoin.adapter = adapterTrendingCoin
        loadData()
        handleData()
    }

    private fun handleData() {
        adapterTrendingCoin.subjectTrending = { data ->
            val intentTrending = Intent(requireActivity(), DetailTokenActivity::class.java)
            intentTrending.putExtra(Constants.EXTRA_SYMBOL_COIN, data.symbol)
            intentTrending.putExtra(Constants.EXTRA_LOGO_COIN, data.thumb)
            intentTrending.putExtra(Constants.EXTRA_NAME_COIN, data.name)
            intentTrending.putExtra(Constants.EXTRA_MARKET_RANK_COIN, data.marketCapRank)
            intentTrending.putExtra(Constants.EXTRA_PRICE_COIN, data.data?.price ?: 0.0)
            intentTrending.putExtra(Constants.EXTRA_PRICE_24H, data.data?.priceChangePercentage24h?.get("btc") ?: 0.0)
            startActivity(intentTrending)
        }
    }

    private fun loadData() {
        showLoading(true)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                trendingCoinViewModel.tokensTrending.collect { coinData ->
                    if (coinData.isNotEmpty()) {
                        val newList = coinData.map { it.item }
                        adapterTrendingCoin.data = newList.toMutableList()
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            if (show) {
                loadingTrendingCoin.visibility = View.VISIBLE
                rvTrendingCoin.visibility = View.INVISIBLE
                if (!loadingTrendingCoin.isAnimating) {
                    loadingTrendingCoin.playAnimation()
                }
            } else {
                if (loadingTrendingCoin.isVisible) {
                    loadingTrendingCoin.postDelayed({
                        loadingTrendingCoin.cancelAnimation()
                        loadingTrendingCoin.visibility = View.GONE
                        rvTrendingCoin.visibility = View.VISIBLE
                    }, 800)
                }
            }
        }
    }
}