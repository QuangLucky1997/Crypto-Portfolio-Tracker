package com.quangtrader.cryptoportfoliotracker.ui.market.toploser

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentGainerOrLoserCoinsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
import com.quangtrader.cryptoportfoliotracker.ui.market.topgainers.AdapterTopGainers
import com.quangtrader.cryptoportfoliotracker.ui.market.topgainers.TopGainersViewModel
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue


@AndroidEntryPoint
class TopLosersFragment : BaseFragment<FragmentGainerOrLoserCoinsBinding>() {
    private val topGainerOrLoserViewModel: TopGainersViewModel by viewModels()
    @Inject lateinit var adapterTopGainers: AdapterTopGainers
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGainerOrLoserCoinsBinding
    get() = FragmentGainerOrLoserCoinsBinding::inflate

    override fun onViewCreated() {
        loadData()
        handleData()
    }

    private fun loadData() {
        topGainerOrLoserViewModel.loadDataGainerOrLoser()
        viewLifecycleOwner.lifecycleScope.launch {
            topGainerOrLoserViewModel.tokensGainerOrLoser.collect { coinData ->
                val sortedData = coinData.sortedBy { it.priceChangePercentage24h }
                val topGainers = sortedData.take(20)
                adapterTopGainers.data.clear()
                adapterTopGainers.data.addAll(topGainers)
                binding.rvGainerLoserCoin.adapter = adapterTopGainers
            }
        }
    }

    private fun handleData() {
        adapterTopGainers.subjectGainers = { data ->
            val intentTrending = Intent(requireActivity(), DetailTokenActivity::class.java)
            intentTrending.putExtra(Constants.EXTRA_SYMBOL_COIN, data.symbol)
            intentTrending.putExtra(Constants.EXTRA_LOGO_COIN, data.image)
            intentTrending.putExtra(Constants.EXTRA_NAME_COIN, data.name)
            intentTrending.putExtra(Constants.EXTRA_MARKET_RANK_COIN, data.marketCapRank)
            intentTrending.putExtra(Constants.EXTRA_PRICE_COIN, data.currentPrice ?: 0.0)
            intentTrending.putExtra(Constants.EXTRA_PRICE_24H, data.priceChangePercentage24h)
            startActivity(intentTrending)
        }
    }
}