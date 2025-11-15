package com.quangtrader.cryptoportfoliotracker.ui.portfolio

import android.annotation.SuppressLint

import com.quangtrader.cryptoportfoliotracker.databinding.ActivityAddTransactionBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

import androidx.core.view.isGone
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import com.quangtrader.cryptoportfoliotracker.utils.formatPrice
import com.quangtrader.cryptoportfoliotracker.utils.showKeyboard

@AndroidEntryPoint
class DetailAddTransactionActivity :
    BaseActivity<ActivityAddTransactionBinding>(ActivityAddTransactionBinding::inflate) {
    @SuppressLint("SetTextI18n")

    var isUSD = false
    override fun onCreateView() {
        super.onCreateView()
        val dataPrice = intent.getDoubleExtra(Constants.EXTRA_PRICE_COIN, 0.0)
        val dataSymbol = intent.getStringExtra(Constants.EXTRA_SYMBOL_COIN)
        val dataNameCoin = intent.getStringExtra(Constants.EXTRA_NAME_COIN)
        val dataLogo = intent.getStringExtra(Constants.EXTRA_LOGO_COIN)
        binding.apply {
            backImg.clicks {
                onBackPressedDispatcher.onBackPressed()
            }
            tokenName.text = dataNameCoin
            textPrice.text = "${"$".plus(dataPrice.formatPrice(2))} per coin"
            textSymbol.text = dataSymbol
            Glide.with(this@DetailAddTransactionActivity).load(dataLogo).into(imgToken)
            edtValueToken.requestFocus()
            showKeyboard(this@DetailAddTransactionActivity, edtValueToken)
        }
        dataSymbol?.let { initChangeView(it) }
    }


    private fun initChangeView(dtSymbol: String) {
        binding.apply {
            viewSell.clicks {
                cardBuy.isGone = true
                cardSell.isGone = false
            }
            viewBuy.clicks {
                cardBuy.isGone = false
                cardSell.isGone = true
            }

            viewChange.clicks {
                isUSD = !isUSD
                if (isUSD) {
                    textSymbol.text = "USD"
                    symbolUSD.isGone = true
                    textSymbolToken.isGone = false
                    textSymbolToken.text = dtSymbol
                } else {
                    textSymbol.text = dtSymbol
                    symbolUSD.isGone = false
                    textSymbolToken.isGone = true
                }
            }
        }
    }


}