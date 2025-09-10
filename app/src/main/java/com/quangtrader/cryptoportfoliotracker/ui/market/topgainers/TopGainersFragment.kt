package com.quangtrader.cryptoportfoliotracker.ui.market.topgainers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentGainerOrLoserCoinsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TopGainersFragment : BaseFragment<FragmentGainerOrLoserCoinsBinding>() {
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
                val sortedData = coinData.sortedByDescending { it.priceChangePercentage24h }
                val topGainers = sortedData.take(20)
                adapterTopGainers.data.clear()
                adapterTopGainers.data.addAll(topGainers)
                binding.rvGainerLoserCoin.adapter = adapterTopGainers
            }
        }
    }


}