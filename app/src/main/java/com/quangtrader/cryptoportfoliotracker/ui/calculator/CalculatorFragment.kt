package com.quangtrader.cryptoportfoliotracker.ui.calculator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCalculatorProfitBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import com.quangtrader.cryptoportfoliotracker.utils.formatCurrency
import com.quangtrader.cryptoportfoliotracker.utils.formatPercentage
import com.quangtrader.cryptoportfoliotracker.utils.parseToBigDecimal
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@AndroidEntryPoint
class CalculatorFragment : BaseFragment<FragmentCalculatorProfitBinding>() {


    @Inject
    lateinit var adapterListTokenSpinner: AdapterListTokenSpinner
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCalculatorProfitBinding
        get() = FragmentCalculatorProfitBinding::inflate

    override fun onViewCreated() {
        calculatorProfitOrLose()
    }


    @SuppressLint("SetTextI18n")
    private fun calculatorProfitOrLose() {
        binding.apply {
            val priceMarket =
                requireActivity().intent.getDoubleExtra(Constants.EXTRA_PRICE_COIN, 0.0)
            val symbolToken = requireActivity().intent.getStringExtra(Constants.EXTRA_SYMBOL_COIN)
            edtSearchToken.setText(symbolToken)
            edtPriceBuy.setText(priceMarket.toString())
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
                    tvProfitData.text =
                        "${formatCurrency(NPL)} (${formatPercentage(profitPercent)})"
                    tvTotalAmount.text = formatCurrency(TIA)
                    tvTotalExitAmount.text = formatCurrency(TEA)
                    if (profit > BigDecimal.ZERO) {
                        cardColorProfitOrLoss.setCardBackgroundColor(requireActivity().getColor(R.color.green))
                        tvIndicator.setImageResource(R.drawable.up_trend)
                        tvProfitData.setTextColor(requireActivity().getColor(R.color.greenAlpha))
                    } else {
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