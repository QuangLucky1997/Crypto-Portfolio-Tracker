package com.quangtrader.cryptoportfoliotracker.ui.portfolio

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityListTransactionBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
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
    }
}