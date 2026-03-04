package com.quangtrader.cryptoportfoliotracker.ui.market.topgainers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinWrapper
import com.quangtrader.cryptoportfoliotracker.data.remote.GainerOrLoserCoinGeckoResponse
import com.quangtrader.cryptoportfoliotracker.data.repository.TopGainersOrLosersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopGainersViewModel @Inject constructor(private val topGainerOrLoserRepository: TopGainersOrLosersRepository) :
    ViewModel() {
    private val _tokensGainerOrLoser =
        MutableStateFlow<TopGainersUiState>(TopGainersUiState.Loading)
    val tokensGainerOrLoser: StateFlow<TopGainersUiState> =
        _tokensGainerOrLoser


    fun loadDataGainerOrLoser() {
        viewModelScope.launch {
            _tokensGainerOrLoser.value = TopGainersUiState.Loading
            try {
                val response = topGainerOrLoserRepository.getTopGainerOrLoser()
                _tokensGainerOrLoser.value = TopGainersUiState.Success(response)
            } catch (e: Exception) {
                _tokensGainerOrLoser.value = TopGainersUiState.Error(e)
                Log.d("MainError", e.message.toString())
            }
        }
    }
}

sealed class TopGainersUiState {
    object Loading : TopGainersUiState()
    data class Success(val data: List<GainerOrLoserCoinGeckoResponse>) : TopGainersUiState()
    data class Error(val exception: Throwable) : TopGainersUiState()
}