package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.InfoToken
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
    private val _infoToken = MutableStateFlow<InfoToken?>(null)
    val infoToken: StateFlow<InfoToken?> = _infoToken


    fun getAllInfoToken(idToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = overviewRepository.getOverViewInfo(idToken)
                _infoToken.value = response
            } catch (ex: Exception) {
                Log.e("OverviewViewModel", "Error fetching token info", ex)
            }
        }
    }
}