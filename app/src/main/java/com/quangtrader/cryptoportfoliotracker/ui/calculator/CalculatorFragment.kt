package com.quangtrader.cryptoportfoliotracker.ui.calculator

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCalculatorProfitBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.coin.CoinViewModel
import com.quangtrader.cryptoportfoliotracker.utils.formatCurrency
import com.quangtrader.cryptoportfoliotracker.utils.formatPercentage
import com.quangtrader.cryptoportfoliotracker.utils.formatPrice
import com.quangtrader.cryptoportfoliotracker.utils.formatPriceCustom
import com.quangtrader.cryptoportfoliotracker.utils.parseToBigDecimal
import com.quangtrader.cryptoportfoliotracker.utils.textChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@AndroidEntryPoint
class CalculatorFragment : BaseFragment<FragmentCalculatorProfitBinding>() {
    private val coinViewModel by viewModels<CoinViewModel>()


    @Inject
    lateinit var adapterListTokenSpinner: AdapterListTokenSpinner
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCalculatorProfitBinding
        get() = FragmentCalculatorProfitBinding::inflate

    override fun onViewCreated() {
        initEvent()
        loadDataToken()
        calculatorProfitOrLose()
    }


    private fun loadDataToken() {
        binding.rvListToken.adapter = adapterListTokenSpinner
        binding.rvListToken.setHasFixedSize(true)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                coinViewModel.coins
                    .debounce(100)
                    .collect { list ->
                        if (list.isEmpty()) {
                            binding.rvListToken.visibility = View.GONE
                        } else {
                            binding.rvListToken.visibility = View.VISIBLE
                            adapterListTokenSpinner.data = list as MutableList<CoinUI>
                        }
                    }
            }
        }

        coinViewModel.loadCoins()
    }

    private fun initEvent() {
        binding.apply {
            edtSearchToken.clicks {
                rvListToken.visibility = View.VISIBLE
            }
            adapterListTokenSpinner.subjectChooseToken = {
                edtSearchToken.setText(it.symbol)
                Glide.with(requireActivity())
                    .load(it.logo)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            edtSearchToken.setCompoundDrawablesWithIntrinsicBounds(
                                resource,
                                null,
                                null,
                                null
                            )
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
                edtPriceBuy.setText(formatPriceCustom(it.price))
                rvListToken.visibility = View.GONE
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculatorProfitOrLose() {
        binding.apply {
            cardCalculator.clicks {
                val dataPriceBuy = edtPriceBuy.text.toString()
                val dataPriceSell = edtPriceSell.text.toString()
                val dataInvestment = edtInvest.text.toString()
                val dataInvestFee = edtInvestFee.text.toString()
                val dataExitFee = edtExitFee.text.toString()
                if (dataPriceBuy.isNotEmpty() && dataPriceSell.isNotEmpty() && dataInvestment.isNotEmpty()) {
                    val priceBuy = dataPriceBuy.parseToBigDecimal()
                    val priceSell = dataPriceSell.parseToBigDecimal()
                    val investment = dataInvestment.parseToBigDecimal()
                    val investFee = dataInvestFee.parseToBigDecimal() ?: BigDecimal.ZERO
                    val investOut = dataExitFee.parseToBigDecimal() ?: BigDecimal.ZERO
                    if (priceBuy == null || priceSell == null || investment == null) {
                        return@clicks
                    }
                    if (priceBuy.compareTo(BigDecimal.ZERO) == 0) {
                        return@clicks
                    }
                    val quantity = investment.divide(priceBuy, 8, RoundingMode.HALF_UP)
                    val priceDifference = priceSell.subtract(priceBuy)
                    val profit = priceDifference.multiply(quantity)
                    val profitPercent = profit
                        .divide(investment, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal("100"))
                    val totalFees = investFee.add(investOut)
                    val NPL = profit.subtract(totalFees)
                    val TIA = investment.add(investFee)
                    val TEA = priceSell.multiply(quantity).subtract(investOut)
                    tvProfitData.text = "${formatCurrency(NPL)} (${formatPercentage(profitPercent)})"
                    tvTotalAmount.text = formatCurrency(TIA)
                    tvTotalExitAmount.text = formatCurrency(TEA)
                    if(profit > BigDecimal.ZERO){
                        cardColorProfitOrLoss.setCardBackgroundColor(requireActivity().getColor(R.color.green))
                        tvIndicator.setImageResource(R.drawable.up_trend)
                        tvProfitData.setTextColor(requireActivity().getColor(R.color.greenAlpha))
                    }else
                    {
                        cardColorProfitOrLoss.setCardBackgroundColor(requireActivity().getColor(R.color.redAlpha))
                        tvIndicator.setImageResource(R.drawable.down_trend)
                        tvProfitData.setTextColor(requireActivity().getColor(R.color.red))
                    }
                }
            }
        }
    }

    private fun isDataValid(buyPrice: String, sellPrice: String, investment: String): Boolean {
        return buyPrice.isNotBlank() && buyPrice.toBigDecimalOrNull() != null &&
                sellPrice.isNotBlank() && sellPrice.toBigDecimalOrNull() != null &&
                investment.isNotBlank() && investment.toBigDecimalOrNull() != null
    }
}