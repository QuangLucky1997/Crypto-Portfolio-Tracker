package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.chatbot.ChatBotMessage
import com.quangtrader.cryptoportfoliotracker.data.repository.GeminiChatBotRepository
import com.quangtrader.cryptoportfoliotracker.data.repository.ManagerDBRoomRepository
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.data.roommodel.HistoryChatBotEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ChatBotViewModel @Inject constructor(
    private val geminiRepository: GeminiChatBotRepository,
    private val chatBotRepositoryDatabase: ManagerDBRoomRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatBotMessage>>(emptyList())
    val messages: StateFlow<List<ChatBotMessage>> = _messages.asStateFlow()


    val getAllHistoryChat = chatBotRepositoryDatabase.getAllHistoryChatBot.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        showWelcomeMessage()
    }


    fun addHistoryChat(historyChat: HistoryChatBotEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = chatBotRepositoryDatabase.checkIfExists(historyChat.question).first()
            if (count <= 0) {
                chatBotRepositoryDatabase.saveHistoryChat(historyChat)
            }
        }
    }

    fun sendMessage(userText: String) {
        val userMsg = ChatBotMessage.User(userText)
        _messages.update { it + userMsg }

        val botTimestamp = System.currentTimeMillis()
        addBotLoading(botTimestamp)

        viewModelScope.launch {
            try {
                val prompt = buildPrompt(userText)
                val response = geminiRepository.analyze(prompt)

                updateBotMessage(
                    timestamp = botTimestamp,
                    text = response,
                    isLoading = false
                )
            } catch (e: Exception) {
                Timber.e(e, "Bot analyze failed")

                updateBotMessage(
                    timestamp = botTimestamp,
                    text = when (e) {
                        is java.net.UnknownHostException ->
                            "⚠️ No internet connection."

                        else ->
                            "⚠️ AI service error. Please try again."
                    },
                    isLoading = false,
                    isError = true
                )
            }
        }
    }


    private suspend fun buildPrompt(userText: String): String {
        val symbol = extractSymbol(userText) ?: return userText

        val marketData = geminiRepository.analyze(
            "Provide recent technical market data for token $symbol"
        )

        return """
            You are a professional crypto trader.
            
            Token: $symbol
            Timeframe: D1
            Market Data:
            $marketData

            Provide:
            - Trend analysis
            - Key support/resistance
            - Buy/Sell zone
            - Risk warning
        """.trimIndent()
    }

    private fun addBotLoading(timestamp: Long) {
        val botLoading = ChatBotMessage.Bot(
            text = "",
            timestamp = timestamp,
            isLoading = true
        )
        _messages.update { it + botLoading }
    }

    private fun updateBotMessage(
        timestamp: Long,
        text: String,
        isLoading: Boolean,
        isError: Boolean = false
    ) {
        _messages.update { list ->
            list.map {
                if (it is ChatBotMessage.Bot && it.timestamp == timestamp) {
                    it.copy(
                        text = text,
                        isLoading = isLoading,
                        isError = isError
                    )
                } else it
            }
        }
    }

    private fun extractSymbol(text: String): String? {
        return Regex("\\b[A-Z]{3,5}\\b")
            .find(text.uppercase())
            ?.value
    }

    private fun showWelcomeMessage() {
        _messages.value = listOf(
            ChatBotMessage.Bot(
                text = "Hello! I'm QuantAI 🤖\nI can analyze buy & sell zones for crypto tokens.",
                timestamp = System.currentTimeMillis()
            )
        )
    }
}



