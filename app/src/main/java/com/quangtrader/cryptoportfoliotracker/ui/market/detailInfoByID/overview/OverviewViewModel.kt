package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.InfoToken
import com.quangtrader.cryptoportfoliotracker.data.remote.InfoTokenDetail
import com.quangtrader.cryptoportfoliotracker.data.remote.InfoTokenResponse
import com.quangtrader.cryptoportfoliotracker.data.repository.OverviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(private val overviewRepository: OverviewRepository) :
    ViewModel() {
    private val _infoToken = MutableStateFlow<InfoTokenResponse?>(null)
    val infoToken: StateFlow<InfoTokenResponse?> = _infoToken


    fun getAllInfoToken(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = overviewRepository.getFullInfoToken(symbol)
                _infoToken.value = response
            } catch (ex: Exception) {
                Log.e("OverviewViewModel", "Error fetching token info", ex)
            }
        }
    }
}