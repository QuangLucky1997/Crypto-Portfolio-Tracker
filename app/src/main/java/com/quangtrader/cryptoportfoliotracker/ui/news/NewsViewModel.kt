package com.quangtrader.cryptoportfoliotracker.ui.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.remote.NewsResponse
import com.quangtrader.cryptoportfoliotracker.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {
    private val news: MutableStateFlow<List<NewsResponse>> = MutableStateFlow(emptyList())
    var dataNews: StateFlow<List<NewsResponse>> = news

    fun getAllNewsByType(typeData: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = newsRepository.getNewsByType(typeData)
                news.value = data
            } catch (ex: Exception) {
                Log.e("NewsViewModel", ex.message.toString())
            }
        }
    }
}