package com.quangtrader.cryptoportfoliotracker.ui.market.toploser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentGainerOrLoserCoinsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.topgainers.AdapterTopGainers
import com.quangtrader.cryptoportfoliotracker.ui.market.topgainers.TopGainersViewModel
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
}