package com.quangtrader.cryptoportfoliotracker.ui.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.NewsResponse
import com.quangtrader.cryptoportfoliotracker.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<NewsViewUiState>(NewsViewUiState.Success(emptyList()))
    val uiState: StateFlow<NewsViewUiState> = _uiState

    fun getAllNewsByType(typeData: String) {
        viewModelScope.launch {
            _uiState.value = NewsViewUiState.Loading
            try {
                val data = newsRepository.getNewsByType(typeData)
                _uiState.value = NewsViewUiState.Success(data)
            } catch (ex: Exception) {
                _uiState.value = NewsViewUiState.Error(ex)
                Log.e("NewsViewModel", "Error loading news", ex)
            }
        }
    }
}


sealed class NewsViewUiState {
    object Loading : NewsViewUiState()
    data class Success(val data: List<NewsResponse>) : NewsViewUiState()
    data class Error(val exception: Throwable) : NewsViewUiState()
}