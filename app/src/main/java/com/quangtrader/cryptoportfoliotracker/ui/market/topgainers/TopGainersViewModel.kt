package com.quangtrader.cryptoportfoliotracker.ui.market.topgainers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        MutableStateFlow<List<GainerOrLoserCoinGeckoResponse>>(emptyList())
    val tokensGainerOrLoser: StateFlow<List<GainerOrLoserCoinGeckoResponse>> =
        _tokensGainerOrLoser


    fun loadDataGainerOrLoser() {
        viewModelScope.launch {
            try {
                val response = topGainerOrLoserRepository.getTopGainerOrLoser()
                _tokensGainerOrLoser.value = response
            } catch (e: Exception) {
                Log.d("MainError", e.message.toString())
            }
        }
    }
}