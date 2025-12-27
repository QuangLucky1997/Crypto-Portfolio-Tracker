package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.quangtrader.cryptoportfoliotracker.data.chatbot.ChatBotMessage
import com.quangtrader.cryptoportfoliotracker.data.repository.GeminiChatBotRepository
import com.quangtrader.cryptoportfoliotracker.data.repository.ManagerDBRoomRepository
import com.quangtrader.cryptoportfoliotracker.data.roommodel.HistoryChatBotEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ChatBotViewModel @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val geminiChatBotRepository: GeminiChatBotRepository,
    private val chatBotRepositoryDatabase: ManagerDBRoomRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatBotMessage>>(emptyList())
    val messages: StateFlow<List<ChatBotMessage>> = _messages.asStateFlow()

    val getAllSessionChat = chatBotRepositoryDatabase.getAllHistoryChatBot.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList())
    private var chatSession = generativeModel.startChat()

    init {
        viewModelScope.launch {
            delay(100)
            showWelcomeMessage()
        }
    }

    fun sendMessage(userText: String) {
        val userMsg = ChatBotMessage.User(userText)
        _messages.value = _messages.value + userMsg
        val botTimestamp = System.currentTimeMillis()
        val botLoadingMsg = ChatBotMessage.Bot("", isLoading = true, timestamp = botTimestamp)
        _messages.value = _messages.value + botLoadingMsg

        viewModelScope.launch {
            try {
                val symbol = extractSymbol(userText)
                val enrichedPrompt = if (symbol != null) {
                    val dataToken = geminiChatBotRepository.getChatResponse(symbol).first()
                    """
            Analyze the following market data for: $symbol
            Timeframe: D1
            Market Data: $dataToken
            Based on this data, provide a professional technical outlook.
            """.trimIndent()
                } else {
                    userText
                }
                var fullText = ""
                chatSession.sendMessageStream(enrichedPrompt).collect { chunk ->
                    fullText += chunk.text
                    updateBotMessage(botTimestamp, fullText, isLoading = false)
                }
            } catch (e: Exception) {
                Timber.d(e.printStackTrace().toString())
            }
        }
    }

    fun saveHistoryChat(chatBot: HistoryChatBotEntity) {
        viewModelScope.launch {
            try {
                chatBotRepositoryDatabase.saveHistoryChat(chatBot)
            } catch (e: Exception) {
                Timber.e("Save history failed: ${e.message}")
            }
        }
    }

    private fun updateBotMessage(
        timestamp: Long,
        text: String,
        isLoading: Boolean = false,
        isError: Boolean = false
    ) {
        _messages.value = _messages.value.map {
            if (it is ChatBotMessage.Bot && it.timestamp == timestamp) {
                it.copy(text = text, isLoading = isLoading, isError = isError)
            } else it
        }
    }


    private fun extractSymbol(text: String): String? {
        val regex = Regex("\\b[A-Z]{3,5}\\b")
        val match = regex.find(text.uppercase())
        return match?.value
    }

    private fun showWelcomeMessage() {
        val welcomeMsg = ChatBotMessage.Bot(
            text = "Hello! I'm QuantAI - a financial assistant at Crypto Tracker.\n" +
                    "I can help you analyze buy and sell zones for your token.",
            timestamp = System.currentTimeMillis(),
            isLoading = false
        )
        _messages.value = listOf(welcomeMsg)
    }


}


