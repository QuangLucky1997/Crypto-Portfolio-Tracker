package com.quangtrader.cryptoportfoliotracker.ui.market.coin


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.common.utils.startChartToken
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCoinBinding
import com.quangtrader.cryptoportfoliotracker.helper.Preferences
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CoinFragment : BaseFragment<FragmentCoinBinding>() {
    private val coinViewModel by viewModels<CoinViewModel>()

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
                    }
            }
        }
        coinViewModel.loadCoins()
        handleData()

    }

    private fun handleData() {
        adapterCoin.subjectDetail = { data ->
            val logo = data.logo
            val percent = data.percentChange24h
            val price = data.price
            val mktCap = data.marketCap
            if (logo != null && percent != null && price != null && mktCap != null) {
                startChartToken(
                    true,
                    data.name,
                    logo,
                    data.symbol,
                    percent,
                    price,
                    mktCap
                )
            } else {
                Timber.tag("DATA_ERROR")
                    .e("Null fields: logo=$logo, percent=$percent, price=$price, mktCap=$mktCap")
            }

        }

    }
}