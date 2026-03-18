package com.quangtrader.cryptoportfoliotracker.data.repository

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class GeminiChartAnalysisRepository @Inject constructor(
    private val generativeModel: GenerativeModel
) {
    suspend fun analyzeWithChart(userPrompt: String?, bitmap: Bitmap): String {
        val result = StringBuilder()

        try {
            val finalPrompt = """
                Act as an elite Price Action Trader and Technical Analyst.
                Your mission: Thoroughly analyze the provided chart image.
                
                Requirements:
                1. Market Structure: Identify the current trend (Bullish, Bearish, or Sideways).
                2. S/R Levels: Locate major Support and Resistance zones or Supply/Demand zones.
                3. Candlestick Patterns: Pinpoint significant reversal patterns (e.g., Pin Bar, Engulfing).
                4. Executive Summary: Provide a concise professional outlook.
                User's specific query: ${userPrompt ?: "Please analyze this chart in detail."}
            """.trimIndent()

            val inputContent = content {
                image(bitmap)
                text(finalPrompt)
            }

            generativeModel.generateContentStream(inputContent).collect { chunk ->
                result.append(chunk.text.orEmpty())
            }
        } catch (e: Exception) {
            return "Lỗi hệ thống: ${e.localizedMessage}"
        }

        return result.toString()
    }
}