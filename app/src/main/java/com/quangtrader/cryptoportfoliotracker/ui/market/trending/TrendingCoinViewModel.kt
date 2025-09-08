package com.quangtrader.cryptoportfoliotracker.ui.market.trending

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinWrapper
import com.quangtrader.cryptoportfoliotracker.data.repository.TrendingCoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingCoinViewModel @Inject constructor(private val trendingCoinRepository: TrendingCoinRepository) :
    ViewModel() {
    private val _tokenTrending = MutableStateFlow<List<CoinWrapper>>(emptyList()) // can be change data when call api
    val tokensTrending: StateFlow<List<CoinWrapper>> = _tokenTrending // only listening


    fun loadAllTrendingCoin() {
        viewModelScope.launch {
            try {
                val response = trendingCoinRepository.getTrending()
                _tokenTrending.value = response
            } catch (e: Exception) {
                Log.d("MainError", e.message.toString())
            }

        }
    }
}