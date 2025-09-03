package com.quangtrader.cryptoportfoliotracker.ui.market.coin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinData
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
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
    private val _iconList = MutableStateFlow<List<CoinData>>(emptyList())
    val tokens: StateFlow<List<Data>> = _tokenList
    val icon: StateFlow<List<CoinData>> = _iconList


    private val _coins = MutableStateFlow<List<CoinUI>>(emptyList())
    val coins: StateFlow<List<CoinUI>> = _coins


    fun loadAllToken() {
        viewModelScope.launch {
            try {
                val response = coinRepository.getTokens()
                _tokenList.value = response.data
            } catch (e: Exception) {
                Log.d("MainError", e.message.toString())
            }
        }
    }

    fun loadCoins() {
        viewModelScope.launch {
            try {
                // B1: gọi API lấy list token
                val responseTokens = coinRepository.getTokens(limit = 100)
                // B2: lấy ra tất cả id
                val ids = responseTokens.data.mapNotNull { it.id }
                // B3: gọi API lấy icon theo list id
                val responseIcons = coinRepository.getIconsForTokens(ids)
                // B4: merge lại
                _coins.value = coinRepository.mergeCoins(responseTokens, responseIcons)
            } catch (e: Exception) {
                Log.d("MainError", e.message.toString())
            }
        }
    }




}