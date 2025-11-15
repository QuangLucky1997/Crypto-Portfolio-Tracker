package com.quangtrader.cryptoportfoliotracker.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.GlobalResponse
import com.quangtrader.cryptoportfoliotracker.data.repository.CoinMarketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val coinMarketRepository: CoinMarketRepository) :
    ViewModel() {
    private val _marketCap = MutableStateFlow<GlobalResponse?>(null)
    val marketCap: StateFlow<GlobalResponse?> = _marketCap

    init {
        getMarketCap()
    }

    fun getMarketCap() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = coinMarketRepository.getMarketCap()
                _marketCap.value = response

            } catch (ex: Exception) {
                Log.d("MainError", ex.message.toString())
            }
        }
    }


}