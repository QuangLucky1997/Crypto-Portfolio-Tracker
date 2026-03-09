package com.quangtrader.cryptoportfoliotracker.ui.market.toploser

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
import com.quangtrader.cryptoportfoliotracker.ui.market.coin.CoinViewModel
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
import com.quangtrader.cryptoportfoliotracker.ui.market.topgainers.AdapterTopGainers
import com.quangtrader.cryptoportfoliotracker.ui.market.topgainers.TopGainersUiState
import com.quangtrader.cryptoportfoliotracker.ui.market.topgainers.TopGainersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class TopLosersFragment : BaseFragment<FragmentGainerOrLoserCoinsBinding>() {
    private val topGainerOrLoserViewModel: CoinViewModel by viewModels()
    @Inject
    lateinit var adapterTopGainers: AdapterTopGainers
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGainerOrLoserCoinsBinding
        get() = FragmentGainerOrLoserCoinsBinding::inflate

    override fun onViewCreated() {
        binding.rvGainerLoserCoin.adapter = adapterTopGainers
        binding.rvGainerLoserCoin.setHasFixedSize(true)
        loadData()
        topGainerOrLoserViewModel.loadCoins()
        handleData()
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                topGainerOrLoserViewModel.coins.debounce(100).collect { coinData ->
                    if (coinData.isEmpty()) {
                        binding.loadingDataPg.visibility = View.VISIBLE
                        binding.rvGainerLoserCoin.visibility = View.GONE
                    } else {
                        binding.loadingDataPg.visibility = View.GONE
                        binding.rvGainerLoserCoin.visibility = View.VISIBLE
                        val topGainers = coinData
                            .sortedBy { it.percentChange24h ?: 999.0 }
                            .take(20 )
                        adapterTopGainers.submitList(topGainers)
                    }

                }
            }

        }
    }



    private fun handleData() {
        adapterTopGainers.subjectGainers = { data ->
            val intentTrending = Intent(requireActivity(), DetailTokenActivity::class.java).apply {
                putExtra(Constants.EXTRA_SYMBOL_COIN, data.symbol)
                putExtra(Constants.EXTRA_LOGO_COIN, data.logo)
                putExtra(Constants.EXTRA_NAME_COIN, data.name)
                putExtra(Constants.EXTRA_MARKET_RANK_COIN, data.marketCap)
                putExtra(Constants.EXTRA_PRICE_COIN, data.price ?: 0.0)
                putExtra(Constants.EXTRA_PRICE_24H, data.percentChange24h)
            }
            startActivity(intentTrending)
        }
    }
}