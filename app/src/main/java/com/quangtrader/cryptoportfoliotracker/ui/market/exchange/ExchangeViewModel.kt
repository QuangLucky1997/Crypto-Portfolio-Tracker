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
    private val _exchange = MutableStateFlow<TickerResponse?>(null)
    var exchange: StateFlow<TickerResponse?> = _exchange

    fun getExchangeByIdCoin(idCoin:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = exchangeRepository.getExchange(idCoin)
                _exchange.value = response
            } catch (ex: Exception) {
                Log.d("Main123", ex.message.toString())
            }
        }
    }
}
