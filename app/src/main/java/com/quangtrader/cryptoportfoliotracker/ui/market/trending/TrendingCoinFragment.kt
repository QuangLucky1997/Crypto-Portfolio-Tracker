package com.quangtrader.cryptoportfoliotracker.ui.market.trending

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinItem
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentTrendingBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
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
        binding.rvTrendingCoin.adapter = adapterTrendingCoin
        loadData()
        trendingCoinViewModel.loadAllTrendingCoin()
        handleData()
    }

    private fun handleData() {
        adapterTrendingCoin.subjectTrending = { data ->
            val intentTrending = Intent(requireActivity(), DetailTokenActivity::class.java).apply {
                putExtra(Constants.EXTRA_SYMBOL_COIN, data.symbol)
                putExtra(Constants.EXTRA_LOGO_COIN, data.thumb)
                putExtra(Constants.EXTRA_NAME_COIN, data.name)
                putExtra(Constants.EXTRA_MARKET_RANK_COIN, data.marketCapRank)
                putExtra(Constants.EXTRA_PRICE_COIN, data.data?.price ?: 0.0)
                putExtra(
                    Constants.EXTRA_PRICE_24H,
                    data.data?.priceChangePercentage24h?.get("btc") ?: 0.0
                )
            }
            startActivity(intentTrending)


        }
    }

    private fun loadData() {
        showLoading(true)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                trendingCoinViewModel.tokensTrendingUiState.collect { coinDataUiState ->
                    when (coinDataUiState) {
                        is TrendingCoinUiState.Loading -> {
                            binding.loadingTrendingCoin.isVisible = true
                        }
                        is TrendingCoinUiState.Success -> {
                            binding.loadingTrendingCoin.isVisible = false
                            val newList = coinDataUiState.data.map { it.item }
                            adapterTrendingCoin.submitList(newList)
                            adapterTrendingCoin.notifyDataSetChanged()
                        }

                        is TrendingCoinUiState.Error -> {
                            binding.loadingTrendingCoin.isVisible = false
                            val errorMsg = coinDataUiState.exception.localizedMessage
                                ?: "Can not load news data"
                            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                        }
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