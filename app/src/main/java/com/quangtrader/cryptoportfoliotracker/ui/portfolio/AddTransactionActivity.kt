package com.quangtrader.cryptoportfoliotracker.ui.portfolio

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityListTransactionBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class AddTransactionActivity :
    BaseActivity<ActivityListTransactionBinding>(ActivityListTransactionBinding::inflate) {
    private val transactionViewModel by viewModels<MergedCoinViewModel>()


    @Inject
    lateinit var adapterAddTransaction: AdapterAddTransaction
    override fun onCreateView() {
        super.onCreateView()
        setData()
        handleEvent()
    }

    private fun setData() {
        lifecycleScope.launch {
            transactionViewModel.mergedCoins.collect { list ->
                adapterAddTransaction.data = list.toMutableList()
                binding.rvListToken.adapter = adapterAddTransaction
            }
        }

    }

    private fun handleEvent() {
        binding.backImg.clicks {
            onBackPressedDispatcher.onBackPressed()
        }
        adapterAddTransaction.subjectTransaction = { data ->
            val intentDetailAddTransactionActivity = Intent(this, DetailAddTransactionActivity::class.java)
            intentDetailAddTransactionActivity.putExtra(Constants.EXTRA_NAME_COIN,data.name)
            intentDetailAddTransactionActivity.putExtra(Constants.EXTRA_LOGO_COIN, data.logo)
            intentDetailAddTransactionActivity.putExtra(Constants.EXTRA_SYMBOL_COIN,data.symbol)
            intentDetailAddTransactionActivity.putExtra(Constants.EXTRA_PRICE_COIN,data.price)
            startActivity(intentDetailAddTransactionActivity)
        }
    }
}