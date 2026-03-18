package com.quangtrader.cryptoportfoliotracker.ui.chartanalysis

import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.common.utils.getSignalTime
import com.quangtrader.cryptoportfoliotracker.common.utils.parseGeminiResponse
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityReultChartAnalysisBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@Suppress("INFERRED_TYPE_VARIABLE_INTO_POSSIBLE_EMPTY_INTERSECTION")
@AndroidEntryPoint
class ResultAnalysisChartActivity : BaseActivity<ActivityReultChartAnalysisBinding>(
    ActivityReultChartAnalysisBinding::inflate
) {
    private val chartAIViewModel by viewModels<ChartAnalysisViewModel>()
    override fun onCreateView() {
        super.onCreateView()
        handleData()
        setData()
    }

    private fun setData() {
        showLoading(true)
        binding.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED)
                {
                    chartAIViewModel.uiState.collect { state ->
                        when (state) {
                            is ChartAnalysisViewModel.GeminiUiState.Idle -> {
                                dataText.text = ""
                            }

                            is ChartAnalysisViewModel.GeminiUiState.Loading -> {
                                showLoading(true)
                                viewTop.isVisible = false
                                viewResult.isVisible = false
                            }

                            is ChartAnalysisViewModel.GeminiUiState.Success -> {
                                showLoading(false)
                                viewTop.isVisible = true
                                viewResult.isVisible = true
                                setResultData(state.result)
                            }

                            is ChartAnalysisViewModel.GeminiUiState.Error -> {
                                binding.apply {
                                    animationLoadingChart.setAnimation(R.raw.error404)
                                    animationLoadingChart.playAnimation()
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private fun handleData() {
        val receivedUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EXTRA_URI, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("TARGET_URI_KEY") as? android.net.Uri
        }
        receivedUri?.let { data ->
            binding.imgChart.setImageURI(data)
            chartAIViewModel.startChartAnalysis(data)
        }
        binding.backImg.clicks {
            finish()
        }
    }


    private fun showLoading(show: Boolean) {
        binding.apply {
            if (show) {
                animationLoadingChart.visibility = View.VISIBLE
                viewResult.visibility = View.GONE
                animationLoadingChart.post {
                    if (animationLoadingChart.isVisible) {
                        animationLoadingChart.playAnimation()
                    }
                }
            } else {
                animationLoadingChart.visibility = View.GONE
                viewResult.visibility = View.VISIBLE
                animationLoadingChart.cancelAnimation()
            }
        }
    }

    private fun setResultData(dtTrading: String) {
        val dataTrading = parseGeminiResponse(dtTrading)
        dataTrading?.let {
            binding.apply {
                textTime.text = getSignalTime()
                textToken.text = dataTrading.asset
                textDataStructure.text = dataTrading.structure
                textSignal.text = dataTrading.signal
                when (dataTrading.signal.uppercase()) {
                    "BUY" -> {
                        textSignal.setTextColor(getResources().getColor(R.color.green))
                    }

                    "SELL" -> {
                        textSignal.setTextColor(getResources().getColor(R.color.red))
                    }
                }
                textEntry.text = dataTrading.entry.toString()
                textStoploss.text = dataTrading.stop_loss.toString()
                textTp1.text = dataTrading.tp1.toString()
                textTp2.text = dataTrading.tp2.toString()
                textRiskReward.text = dataTrading.risk_reward
                textConfidence.text = dataTrading.confidence.toString().plus("%")
                textDataCandleStickPattern.text = dataTrading.candle_pattern
                textDataKeyLevel.text = dataTrading.key_levels
                textDataTrend.text = dataTrading.trend

            }
        }

    }
}


