package com.quangtrader.cryptoportfoliotracker.ui.market.coin

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCoinBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
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
        coinViewModel.loadAllToken()
        loadData()
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            coinViewModel.tokens.collect { list ->
                Log.d("Main123",list.size.toString())
                adapterCoin.data = list.toMutableList()
            }
        }

    }
}