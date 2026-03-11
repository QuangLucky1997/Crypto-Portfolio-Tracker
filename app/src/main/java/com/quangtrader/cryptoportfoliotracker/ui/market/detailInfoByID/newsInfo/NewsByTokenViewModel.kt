package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.newsInfo

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
    private val newsByToken: MutableStateFlow<NewsUiState> =
        MutableStateFlow(NewsUiState.Loading)
    var dataNews: StateFlow<NewsUiState> = newsByToken
    fun getAllNewsByToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            newsByToken.value = NewsUiState.Loading
            try {
                val dtNews = newsRepository.getNewsByToken(token)
                newsByToken.value = NewsUiState.Success(dtNews)
            } catch (ex: Exception) {
                newsByToken.value = NewsUiState.Error(ex)
            }

        }
    }
}

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val data: NewsByTokenResponse) : NewsUiState()
    data class Error(val exception: Throwable) : NewsUiState()
}