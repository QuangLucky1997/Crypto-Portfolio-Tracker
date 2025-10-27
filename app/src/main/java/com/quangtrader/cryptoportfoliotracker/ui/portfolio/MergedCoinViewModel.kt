package com.quangtrader.cryptoportfoliotracker.ui.portfolio

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.local.CoinTransaction
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
import com.quangtrader.cryptoportfoliotracker.data.repository.CoinRepository
import com.quangtrader.cryptoportfoliotracker.data.repository.ManagerDBRoomRepository
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class MergedCoinViewModel @Inject constructor(
    private val coinRepository: CoinRepository,          // API
    private val managerDBRoomRepository: ManagerDBRoomRepository // ROOM
) : ViewModel() {

    private val _mergedCoins = MutableStateFlow<List<CoinTransaction>>(emptyList())
    val mergedCoins: StateFlow<List<CoinTransaction>> = _mergedCoins

    init {
        loadMergedData()
    }

    private fun loadMergedData() {
        viewModelScope.launch {
            val localFlow = managerDBRoomRepository.getAllFavCoin
            val apiFlow = flow {
                try {
                    val tokenResponse = coinRepository.getTokens(limit = 100)
                    val ids = tokenResponse.data.mapNotNull { it.id }
                    val iconsResponse = coinRepository.getIconsForTokens(ids)
                    val mergedCoins = coinRepository.mergeCoins(tokenResponse, iconsResponse)
                    emit(mergedCoins)
                } catch (e: Exception) {
                    Log.e("MergedVM", "API error: ${e.message}")
                    emit(emptyList())
                }
            }
            combine(localFlow, apiFlow) { localCoins, apiCoins ->
                mergeToTransactionList(localCoins, apiCoins as ArrayList<CoinUI>)
            }
                .catch { e -> Log.e("MergedVM", "Combine error: ${e.message}") }
                .collect { mergedList ->
                    _mergedCoins.value = mergedList
                }
        }
    }

    private fun mergeToTransactionList(
        localCoins: List<CoinFav>,
        apiCoins: ArrayList<CoinUI>
    ): List<CoinTransaction> {
        val merged = mutableListOf<CoinTransaction>()

        if (localCoins.isNotEmpty()) {
            merged.add(CoinTransaction.Header("Add from watchlist"))
            merged.addAll(localCoins.map {
                CoinTransaction.CoinItem(
                    name = it.name,
                    symbol = it.symbol,
                    logo = it.logo,
                    source = CoinTransaction.Source.ROOM
                )
            })
        }

        if (apiCoins.isNotEmpty()) {
            merged.add(CoinTransaction.Header("All coins"))
            merged.addAll(apiCoins.map {
                CoinTransaction.CoinItem(
                    name = it.name,
                    symbol = it.symbol,
                    logo = it.logo,
                    source = CoinTransaction.Source.API
                )
            })
        }

        return merged
    }
}