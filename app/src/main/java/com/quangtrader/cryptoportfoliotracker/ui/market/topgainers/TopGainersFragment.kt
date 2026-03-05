package com.quangtrader.cryptoportfoliotracker.ui.market.topgainers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentGainerOrLoserCoinsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TopGainersFragment : BaseFragment<FragmentGainerOrLoserCoinsBinding>() {
    private val topGainerOrLoserViewModel: TopGainersViewModel by viewModels()

    @Inject
    lateinit var adapterTopGainers: AdapterTopGainers
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGainerOrLoserCoinsBinding
        get() = FragmentGainerOrLoserCoinsBinding::inflate

    override fun onViewCreated() {
        binding.rvGainerLoserCoin.adapter = adapterTopGainers
        loadData()
        handleData()
    }

    private fun loadData() {
        topGainerOrLoserViewModel.loadDataGainerOrLoser()
        showLoading(true)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                topGainerOrLoserViewModel.tokensGainerOrLoser.collect { coinData ->
                    when (coinData) {
                        is TopGainersUiState.Loading -> {
                            showLoading(true)
                        }

                        is TopGainersUiState.Success -> {
                            showLoading(false)
                            val sortedData = coinData.data.sortedByDescending { it.priceChangePercentage24h }
                            val topGainers = sortedData.take(20)
                            adapterTopGainers.submitList(topGainers)
                        }

                        is TopGainersUiState.Error -> {
                            binding.animationLoading.setAnimation(R.raw.error404)
                            binding.animationLoading.playAnimation()
                        }
                    }
                }
            }

        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            if (show) {
                animationLoading.visibility = View.VISIBLE
                rvGainerLoserCoin.visibility = View.GONE
                animationLoading.post {
                    if (animationLoading.isVisible) {
                        animationLoading.playAnimation()
                    }
                }
            } else {
                animationLoading.visibility = View.GONE
                rvGainerLoserCoin.visibility = View.VISIBLE
                animationLoading.cancelAnimation()
            }
        }
    }

    private fun handleData() {
        adapterTopGainers.subjectGainers = { data ->
            val intentTrending = Intent(requireActivity(), DetailTokenActivity::class.java).apply {
                putExtra(Constants.EXTRA_SYMBOL_COIN, data.symbol)
                putExtra(Constants.EXTRA_LOGO_COIN, data.image)
                putExtra(Constants.EXTRA_NAME_COIN, data.name)
                putExtra(Constants.EXTRA_MARKET_RANK_COIN, data.marketCapRank)
                putExtra(Constants.EXTRA_PRICE_COIN, data.currentPrice ?: 0.0)
                putExtra(Constants.EXTRA_PRICE_24H, data.priceChangePercentage24h)
            }
            startActivity(intentTrending)
        }
    }
}
