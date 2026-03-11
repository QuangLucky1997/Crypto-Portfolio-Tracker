package com.quangtrader.cryptoportfoliotracker.ui.market.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.TradFiClean
import com.quangtrader.cryptoportfoliotracker.data.remote.whiteListSymbols
import com.quangtrader.cryptoportfoliotracker.data.repository.TradFiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradFiViewModel @Inject constructor(
    private val tradFiRepository: TradFiRepository
) : ViewModel() {
    private val _tradFiState = MutableStateFlow<TradFiUIState>(TradFiUIState.Loading)
    val tradFiState: StateFlow<TradFiUIState> = _tradFiState
    fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _tradFiState.value = TradFiUIState.Loading
            try {
                val tickersDeferred = async { tradFiRepository.getAllTradFi() }
                val contractsDeferred = async { tradFiRepository.getTradFiByDisplayName() }

                val tickersRes = tickersDeferred.await()
                val contractsRes = contractsDeferred.await()

                // 2. Tạo Map từ Contracts để tra cứu displayName nhanh hơn (O(1))
                val contractMap = contractsRes.data.associateBy { it.symbol }

                // 3. Xử lý chuỗi dữ liệu
                val uiModels = tickersRes.data
                    .filter { ticker ->
                        val isUSDT = ticker.symbol?.endsWith("USDT", ignoreCase = true) == true
                        val isInWhiteList = whiteListSymbols.contains(ticker.symbol)
                        isUSDT && isInWhiteList
                    }
                    .mapNotNull { ticker ->
                        val contract = contractMap[ticker.symbol]
                        contract?.let {
                            val percent = ticker.priceChangePercent.toDoubleOrNull() ?: 0.0
                            TradFiClean(
                                symbol = ticker.symbol ?: "",
                                displayName = it.displayName.replace("-USDT", "", true),
                                lastPrice = ticker.lastPrice.toDoubleOrNull() ?: 0.0,
                                priceChangePercent = percent,
                                volume = ticker.volume
                            )
                        }
                    }

                _tradFiState.value = TradFiUIState.Success(uiModels)

            } catch (e: Exception) {
                _tradFiState.value = TradFiUIState.Error(e)
            }
        }
    }
}

sealed class TradFiUIState {
    object Loading : TradFiUIState()
    data class Success(val data: List<TradFiClean>) : TradFiUIState()
    data class Error(val exception: Throwable) : TradFiUIState()
}