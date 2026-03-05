package com.quangtrader.cryptoportfoliotracker.ui.market.exchange

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentExchangeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
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
        binding.rvExchange.adapter = adapterExchange
        setData()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setData() {
        showLoading(true)
        val activity = requireActivity() as DetailTokenActivity
        val tokenID = activity.intent.getStringExtra(Constants.EXTRA_NAME_COIN)
        if (tokenID != null) {
            exchangeViewModel.getExchangeByIdCoin(tokenID.toLowerCase())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exchangeViewModel.exchange.collect { ticketData ->
                    when (ticketData) {
                        is ExchangeUiState.Loading -> {
                            showLoading(true)
                        }

                        is ExchangeUiState.Success -> {
                            showLoading(false)
                            val ticketFilter = ticketData.data.tickers?.filter { it.target == "USDT" }
                            adapterExchange.submitList(ticketFilter)

                        }
                        is ExchangeUiState.Error -> {
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
                rvExchange.visibility = View.GONE
                animationLoading.post {
                    if (animationLoading.isVisible) {
                        animationLoading.playAnimation()
                    }
                }
            } else {
                animationLoading.visibility = View.GONE
                rvExchange.visibility = View.VISIBLE
                animationLoading.cancelAnimation()
            }
        }
    }


}

