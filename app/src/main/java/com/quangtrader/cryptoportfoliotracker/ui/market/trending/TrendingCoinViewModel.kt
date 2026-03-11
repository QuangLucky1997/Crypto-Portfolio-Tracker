package com.quangtrader.cryptoportfoliotracker.ui.market.trending

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinWrapper
import com.quangtrader.cryptoportfoliotracker.data.remote.NewsResponse
import com.quangtrader.cryptoportfoliotracker.data.repository.TrendingCoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingCoinViewModel @Inject constructor(private val trendingCoinRepository: TrendingCoinRepository) :
    ViewModel() {
    private val _tokenTrendingUiState = MutableStateFlow<TrendingCoinUiState>(TrendingCoinUiState.Loading)
    val tokensTrendingUiState: StateFlow<TrendingCoinUiState> = _tokenTrendingUiState


    fun loadAllTrendingCoin() {
        viewModelScope.launch(Dispatchers.IO) {
            _tokenTrendingUiState.value  = TrendingCoinUiState.Loading
            try {
                val response = trendingCoinRepository.getTrending()
                _tokenTrendingUiState.value = TrendingCoinUiState.Success(response)
            } catch (e: Exception) {
                _tokenTrendingUiState.value = TrendingCoinUiState.Error(e)
                Log.d("MainError", e.message.toString())
            }

        }
    }
}

sealed class TrendingCoinUiState {
    object Loading : TrendingCoinUiState()
    data class Success(val data: List<CoinWrapper>) : TrendingCoinUiState()
    data class Error(val exception: Throwable) : TrendingCoinUiState()
}