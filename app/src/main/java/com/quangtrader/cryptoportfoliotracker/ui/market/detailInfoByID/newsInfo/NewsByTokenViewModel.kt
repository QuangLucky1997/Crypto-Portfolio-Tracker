package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.newsInfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.NewsByTokenResponse
import com.quangtrader.cryptoportfoliotracker.data.repository.NewsByTokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsByTokenViewModel @Inject constructor(private val newsRepository: NewsByTokenRepository) :
    ViewModel() {
    private val newsByToken: MutableStateFlow<NewsByTokenResponse> =
        MutableStateFlow(NewsByTokenResponse())
    var dataNews: StateFlow<NewsByTokenResponse> = newsByToken
    fun getAllNewsByToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dataNewsByToken = newsRepository.getNewsByToken(token)
                newsByToken.value = dataNewsByToken
            } catch (ex: Exception) {
                Log.e("ErrorApI", ex.message.toString())
            }
        }
    }
}