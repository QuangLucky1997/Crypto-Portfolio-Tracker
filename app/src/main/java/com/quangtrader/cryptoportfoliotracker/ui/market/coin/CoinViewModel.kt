package com.quangtrader.cryptoportfoliotracker.ui.market.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.Data
import com.quangtrader.cryptoportfoliotracker.data.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(private val coinRepository: CoinRepository) : ViewModel() {
    private val _tokenList = MutableStateFlow<List<Data>>(emptyList())
    val tokens: StateFlow<List<Data>> = _tokenList
    fun loadAllToken() {
        viewModelScope.launch {
            try {
                val response = coinRepository.getTokens()
                _tokenList.value = response.data
            } catch (e: Exception) {

            }
        }
    }
}