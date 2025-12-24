package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.chatbot.ChatBotMessage
import com.quangtrader.cryptoportfoliotracker.data.repository.GeminiChatBotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class ChatBotViewModel @Inject constructor(
    private val geminiChatBotRepository: GeminiChatBotRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatBotMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private var botJob: Job? = null

    init {
        showGreetingIfNeeded()
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        botJob?.cancel()

        // User message
        addMessage(ChatBotMessage.User(text))

        // Bot loading
        addMessage(ChatBotMessage.Bot(text = "", isLoading = true))

        botJob = viewModelScope.launch {
            try {
                val botReply = withTimeout(15_000L) {
                    geminiChatBotRepository.analyzeToken(text)
                }

                removeLastMessage() // remove loading
                addMessage(ChatBotMessage.Bot(text = botReply))

            } catch (e: TimeoutCancellationException) {
                removeLastMessage()
                addMessage(
                    ChatBotMessage.Bot(
                        text = "⚠️ Analysis timed out. Please try again.",
                        isError = true
                    )
                )
            } catch (e: CancellationException) {
            } catch (e: Exception) {
                removeLastMessage()
                addMessage(
                    ChatBotMessage.Bot(
                        text = "⚠️ Unable to analyze at the moment. Please try again later.",
                        isError = true
                    )
                )
            }
        }

    }
    private fun showGreetingIfNeeded() {
        if (_messages.value.isNotEmpty()) return

        addMessage(
            ChatBotMessage.Bot(
                text = """
👋 Hello! I’m an AI token analysis assistant.
My role is to analyze cryptocurrency tokens using pure price action.
Enter a token symbol (e.g. BTC, ETH, SOL) to get started.
⚠️ This analysis is for informational purposes only.
                """.trimIndent()
            )
        )
    }

    private fun addMessage(message: ChatBotMessage) {
        _messages.value = _messages.value + message
    }

    private fun removeLastMessage() {
        _messages.value = _messages.value.dropLast(1)
    }
}


