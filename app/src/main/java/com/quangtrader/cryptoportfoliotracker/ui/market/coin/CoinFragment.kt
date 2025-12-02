package com.quangtrader.cryptoportfoliotracker.ui.market.coin


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.data.roommodel.TokenTop100
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCoinBinding
import com.quangtrader.cryptoportfoliotracker.helper.Preferences
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.calculator.CalculatorViewModel
import com.quangtrader.cryptoportfoliotracker.ui.market.coin.detailCoin.ChartTokenActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CoinFragment : BaseFragment<FragmentCoinBinding>() {
    private val coinViewModel by viewModels<CoinViewModel>()

    private val calculatorViewModel by viewModels<CalculatorViewModel>()

    @Inject
    lateinit var adapterCoin: AdapterCoin

    @Inject
    lateinit var prefs: Preferences
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCoinBinding
        get() = FragmentCoinBinding::inflate

    @OptIn(FlowPreview::class)
    override fun onViewCreated() {
        binding.rvListToken.adapter = adapterCoin
        binding.rvListToken.setHasFixedSize(true)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                coinViewModel.coins
                    .debounce(100)
                    .collect { list ->
                        if (list.isEmpty()) {
                            binding.loadingDataPg.visibility = View.VISIBLE
                            binding.rvListToken.visibility = View.GONE
                        } else {
                            binding.loadingDataPg.visibility = View.GONE
                            binding.rvListToken.visibility = View.VISIBLE
                            adapterCoin.submitList(list)
                        }
                        if (prefs.isFirstInstall.get()) {
                            for (item in list) {
                                val logoData = item.logo
                                val priceData = item.price
                                if (logoData != null && priceData != null) {
                                    val token = TokenTop100(
                                        0,
                                        item.symbol,
                                        item.name,
                                        priceData,
                                        logoData
                                    )
                                    calculatorViewModel.saveTokenTop100(token)
                                }
                            }
                        }
                    }
            }
        }

        coinViewModel.loadCoins()
        handleData()

    }

    private fun handleData() {
        adapterCoin.subjectDetail = { data ->
            val intent = Intent(requireContext(), ChartTokenActivity::class.java)
            intent.putExtra(Constants.EXTRA_NAME_COIN, data.name)
            intent.putExtra(Constants.EXTRA_LOGO_COIN, data.logo)
            intent.putExtra(Constants.EXTRA_SYMBOL_COIN, data.symbol)
            intent.putExtra(Constants.EXTRA_PERCENT_24_H, data.percentChange24h)
            intent.putExtra(Constants.EXTRA_LOGO, data.logo)
            intent.putExtra(Constants.EXTRA_PRICE_COIN, data.price)
            intent.putExtra(Constants.EXTRA_MARKET_CAP, data.marketCap)
            startActivity(intent)
        }

    }
}