package com.quangtrader.cryptoportfoliotracker.ui.market.coin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCoinBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.coin.detailCoin.ChartTokenActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
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
        adapterCoin.subjectDetail = { data ->
            val intent = Intent(requireContext(), ChartTokenActivity::class.java)
            intent.putExtra(Constants.EXTRA_NAME_COIN, data.name)
            intent.putExtra(Constants.EXTRA_LOGO_COIN, data.logo)
            intent.putExtra(Constants.EXTRA_SYMBOL_COIN, data.symbol)
            startActivity(intent)
        }

    }
}