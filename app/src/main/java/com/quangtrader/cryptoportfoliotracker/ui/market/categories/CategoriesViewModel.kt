package com.quangtrader.cryptoportfoliotracker.ui.market.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import com.quangtrader.cryptoportfoliotracker.data.remote.ResponseCategoryCoin
import com.quangtrader.cryptoportfoliotracker.data.repository.CategoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val categoriesRepository: CategoriesRepository) : ViewModel() {
    private val categoriesToken = MutableStateFlow<List<ResponseCategoryCoin>>(emptyList())
    val categories: StateFlow<List<ResponseCategoryCoin>> = categoriesToken

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val response = categoriesRepository.getCategories()
                categoriesToken.value = response
            } catch (e: Exception) {
                Log.d("MainError", e.message.toString())
            }
        }

    }
}