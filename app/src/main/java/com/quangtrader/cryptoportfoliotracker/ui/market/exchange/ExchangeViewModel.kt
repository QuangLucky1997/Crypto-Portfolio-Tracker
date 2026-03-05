package com.quangtrader.cryptoportfoliotracker.ui.market.exchange

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.TickerResponse
import com.quangtrader.cryptoportfoliotracker.data.repository.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(private val exchangeRepository: ExchangeRepository) :
    ViewModel() {
    private val _exchange = MutableStateFlow<ExchangeUiState>(ExchangeUiState.Loading)
    val exchange: StateFlow<ExchangeUiState> = _exchange


    fun getExchangeByIdCoin(idCoin: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _exchange.value = ExchangeUiState.Loading
            try {
                val response = exchangeRepository.getExchange(idCoin)
                _exchange.value = ExchangeUiState.Success(response)
            } catch (ex: Exception) {
                _exchange.value = ExchangeUiState.Error(ex)
            }
        }
    }
}

sealed class ExchangeUiState {
    object Loading : ExchangeUiState()
    data class Success(val data: TickerResponse) : ExchangeUiState()
    data class Error(val exception: Throwable) : ExchangeUiState()
}
