package com.quangtrader.cryptoportfoliotracker.ui.chartanalysis

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChartAnalysisViewModel @Inject constructor() : ViewModel() {
    private val _captureState = MutableStateFlow<CaptureState>(CaptureState.Idle)
    val captureState = _captureState.asStateFlow()

    fun onImageSaved(uri: Uri?) {
        _captureState.value = CaptureState.Success(uri)
        // Tại đây bạn có thể gọi tiếp logic AI phân tích ảnh
    }

    fun onCaptureError(exc: Exception) {
        _captureState.value = CaptureState.Error(exc.message ?: "Unknown Error")
    }
    
    sealed class CaptureState {
        object Idle : CaptureState()
        data class Success(val uri: Uri?) : CaptureState()
        data class Error(val message: String) : CaptureState()
    }
}