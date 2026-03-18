package com.quangtrader.cryptoportfoliotracker.ui.chartanalysis

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.common.utils.toBitmap
import com.quangtrader.cryptoportfoliotracker.data.repository.GeminiChartAnalysisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChartAnalysisViewModel @Inject constructor(
    private val chartAnalysisRepository: GeminiChartAnalysisRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<GeminiUiState>(GeminiUiState.Idle)
    val uiState: StateFlow<GeminiUiState> = _uiState
    private val _captureState = MutableStateFlow<CaptureState>(CaptureState.Idle)
    val captureState = _captureState.asStateFlow()

    fun onImageSaved(uri: Uri?) {
        _captureState.value = CaptureState.Success(uri)
    }

    fun onCaptureError(exc: Exception) {
        _captureState.value = CaptureState.Error(exc.message ?: "Unknown Error")
    }


    fun startChartAnalysis(uri: Uri, userPrompt: String? = null) {
        viewModelScope.launch {
            _uiState.value = GeminiUiState.Loading
            val result = withContext(Dispatchers.IO) {
                try {
                    val bitmap = uri.toBitmap(context)
                        ?: return@withContext Result.failure(Exception("Không thể đọc dữ liệu ảnh"))
                    val finalPrompt = userPrompt ?: """
                    Act as an elite Price Action Trader. Analyze this chart for:
                    1. Market Structure & Trend (Bullish/Bearish/Sideways).
                    2. Key Support/Resistance & Supply/Demand zones.
                    3. Significant Candlestick Patterns (Pin Bar, Engulfing, etc.).
                    4. Professional Summary.
                """.trimIndent()
                    val analysis = chartAnalysisRepository.analyzeWithChart(finalPrompt, bitmap)
                    Result.success(analysis)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            result.fold(
                onSuccess = { _uiState.value = GeminiUiState.Success(it) },
                onFailure = { _uiState.value = GeminiUiState.Error(it.localizedMessage ?: "Unknown Error") }
            )
        }
    }


    sealed class CaptureState {
        object Idle : CaptureState()
        data class Success(val uri: Uri?) : CaptureState()
        data class Error(val message: String) : CaptureState()
    }

    sealed class GeminiUiState {
        object Idle : GeminiUiState()
        object Loading : GeminiUiState()
        data class Success(val result: String) : GeminiUiState()
        data class Error(val message: String) : GeminiUiState()
    }
}