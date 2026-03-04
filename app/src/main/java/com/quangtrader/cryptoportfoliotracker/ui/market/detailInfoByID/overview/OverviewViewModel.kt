package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.overview


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.common.utils.formatMarketCap
import com.quangtrader.cryptoportfoliotracker.common.utils.formatPercent
import com.quangtrader.cryptoportfoliotracker.data.remote.InfoTokenDetail
import com.quangtrader.cryptoportfoliotracker.data.remote.InfoTokenResponse
import com.quangtrader.cryptoportfoliotracker.data.repository.OverviewRepository
import com.quangtrader.cryptoportfoliotracker.helper.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TokenDisplayState(
    val marketCap: String = "",
    val fullyDilutedValuation: String = "",
    val totalVol: String = "",
    val change1H: String = "",
    val change24H: String = "",
    val change7D: String = "",
    val change30D: String = ""
)

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val overviewRepository: OverviewRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<InfoTokenResponse>>(UiState.Loading)
    val uiState = _uiState.map { state ->
        when (state) {
            is UiState.Success -> {
                val response = state.data
                val tokenDetail = response.data.values.firstOrNull()
                if (tokenDetail != null) {
                    UiState.Success(mapToDisplayState(tokenDetail))
                } else {
                    UiState.Empty
                }
            }

            is UiState.Loading -> UiState.Loading
            is UiState.Error -> UiState.Error(state.message)
            else -> UiState.Empty
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    private fun mapToDisplayState(detail: InfoTokenDetail): TokenDisplayState {
        val usd = detail.quote.USD
        return TokenDisplayState(
            marketCap = formatMarketCap(usd?.marketCap ?: 0.0),
            fullyDilutedValuation = formatMarketCap(usd?.fullyDilutedMarketCap ?: 0.0),
            totalVol = formatMarketCap(usd?.volume24h ?: 0.0),
            change1H = usd?.percentChange1h.formatPercent(),
            change24H = usd?.percentChange24h.formatPercent(),
            change7D = usd?.percentChange7d.formatPercent(),
            change30D = usd?.percentChange30d.formatPercent()
        )
    }

    fun getAllInfoToken(symbol: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = overviewRepository.getFullInfoToken(symbol)
                _uiState.value = UiState.Success(response)
            } catch (ex: Exception) {
                _uiState.value = UiState.Error(ex.message ?: "An unknown error occurred")
            }
        }
    }
}