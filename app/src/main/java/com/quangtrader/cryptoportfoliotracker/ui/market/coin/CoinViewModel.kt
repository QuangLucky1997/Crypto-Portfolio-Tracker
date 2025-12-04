package com.quangtrader.cryptoportfoliotracker.ui.market.coin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinData
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
import com.quangtrader.cryptoportfoliotracker.data.remote.Data
import com.quangtrader.cryptoportfoliotracker.data.remote.ResponseCoinMarket
import com.quangtrader.cryptoportfoliotracker.data.repository.BinanceWebSocketManager
import com.quangtrader.cryptoportfoliotracker.data.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(private val coinRepository: CoinRepository) : ViewModel() {

    private val wsManager = BinanceWebSocketManager()
    private val _coins = MutableStateFlow<List<CoinUI>>(emptyList())

    private val _coinsOnly = MutableStateFlow<List<CoinUI>>(emptyList())
    val coins: StateFlow<List<CoinUI>> = _coins
    val coinsOnlyAPI: StateFlow<List<CoinUI>> = _coinsOnly

    private val coinCache = mutableMapOf<String, CoinUI>()
    private var wsConnected = false

    fun loadCoins() {
        viewModelScope.launch {
            val responseTokens = coinRepository.getTokens(limit = 100)
            val ids = responseTokens.data.mapNotNull { it.id }
            val responseIcons = coinRepository.getIconsForTokens(ids)
            val mergedList = coinRepository.mergeCoins(responseTokens, responseIcons)
            mergedList.forEach { coinCache[it.symbol.lowercase()] = it }
            _coins.value = mergedList
            if (!wsConnected) startWebSocket(mergedList.map { it.symbol.lowercase() + "usdt" })
        }
    }

    fun onlyLoadCoinAPI(){
        viewModelScope.launch {
            val responseTokens = coinRepository.getTokens(limit = 100)
            val ids = responseTokens.data.mapNotNull { it.id }
            val responseIcons = coinRepository.getIconsForTokens(ids)
            val mergedList = coinRepository.mergeCoins(responseTokens, responseIcons)
            mergedList.forEach { coinCache[it.symbol.lowercase()] = it }
            _coinsOnly.value = mergedList
        }
    }

    private fun startWebSocket(symbols: List<String>) {
        wsManager.connect(symbols) { msg ->
            viewModelScope.launch(Dispatchers.Default) {
                val json = JSONObject(msg)
                val data = json.getJSONObject("data")
                val symbol = data.getString("s").lowercase()
                val price = data.getString("c").toDouble()
                val key = symbol.removeSuffix("usdt")
                val old = coinCache[key] ?: return@launch
                val newCoin = old.copy(price = price)
                coinCache[key] = newCoin
                _coins.value = coinCache.values.toList()
            }
        }
        wsConnected = true
    }

    override fun onCleared() {
        super.onCleared()
        wsManager.close()
    }



}