package com.quangtrader.cryptoportfoliotracker.ui.market.exchange

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentExchangeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ExchangeFragment : BaseFragment<FragmentExchangeBinding>() {
    @Inject
    lateinit var adapterExchange: AdapterExchange
    private val exchangeViewModel by viewModels<ExchangeViewModel>()
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentExchangeBinding
        get() = FragmentExchangeBinding::inflate

    override fun onViewCreated() {
        setData()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setData() {
        val activity = requireActivity() as DetailTokenActivity
        val tokenID = activity.intent.getStringExtra(Constants.EXTRA_NAME_COIN)
        if (tokenID != null) {
            exchangeViewModel.getExchangeByIdCoin(tokenID.toLowerCase())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            exchangeViewModel.exchange.collect { coinData ->
                val ticketFilter = coinData?.tickers?.filter { it.target == "USDT" } ?: emptyList()
                adapterExchange.data.clear()
                adapterExchange.data.addAll(ticketFilter)
                binding.rvExchange.adapter = adapterExchange

            }

        }
    }
}
