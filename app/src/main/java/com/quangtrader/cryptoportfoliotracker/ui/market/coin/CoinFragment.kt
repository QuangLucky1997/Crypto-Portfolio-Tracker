package com.quangtrader.cryptoportfoliotracker.ui.market.coin

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCoinBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CoinFragment : BaseFragment<FragmentCoinBinding>() {
    private val coinViewModel by viewModels<CoinViewModel>()

    @Inject
    lateinit var adapterCoin: AdapterCoin
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCoinBinding
        get() = FragmentCoinBinding::inflate

    override fun onViewCreated() {
        coinViewModel.loadCoins()
        loadData()
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            coinViewModel.coins.collect { list ->
                adapterCoin.data = list.toMutableList()
                binding.rvListToken.adapter = adapterCoin

            }
        }

    }
}