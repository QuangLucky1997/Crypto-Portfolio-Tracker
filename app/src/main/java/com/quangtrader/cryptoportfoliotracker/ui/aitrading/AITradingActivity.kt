package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.chatbot.ChatBotMessage
import com.quangtrader.cryptoportfoliotracker.data.roommodel.HistoryChatBotEntity
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityAiTradingBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AITradingActivity : BaseActivity<ActivityAiTradingBinding>(ActivityAiTradingBinding::inflate) {

    private val chatBotViewModel by viewModels<ChatBotViewModel>()

    @Inject
    lateinit var chatAdapter: AdapterChatBotGemini

    override fun onCreateView() {
        super.onCreateView()
        setupRecyclerView()
        observeMessages()
        setupClicks()
    }


    private fun setupRecyclerView() = with(binding.rvChatBot) {
        adapter = chatAdapter
        setHasFixedSize(true)
        itemAnimator = null
        setItemViewCacheSize(20)
    }


    private fun observeMessages() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                chatBotViewModel.messages.collectLatest { messages ->
                    val shouldScroll = binding.rvChatBot.isAtBottom()
                    chatAdapter.submitList(messages) {
                        if (shouldScroll && messages.isNotEmpty()) {
                            binding.rvChatBot.scrollToPosition(messages.lastIndex)
                        }
                    }

                    updateSendState(messages)
                }
            }
        }
    }

    private fun updateSendState(messages: List<ChatBotMessage>) {
        val isBotTyping = messages.lastOrNull()
            ?.let { it is ChatBotMessage.Bot && it.isLoading }
            ?: false

        binding.btnSend.isEnabled = !isBotTyping
    }



    private fun setupClicks() = with(binding) {

        btnSend.clicks(debounce = 300) {
            val text = edtMess.text.toString().trim()
            if (text.isNotEmpty()) {
                chatBotViewModel.sendMessage(text)
                val historyChatBot = HistoryChatBotEntity(0, text, System.currentTimeMillis())
                chatBotViewModel.addHistoryChat(historyChatBot)
                edtMess.text.clear()
                val window = this@AITradingActivity.window
                WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())
            }

        }

        imgHistory.clicks(debounce = 500) {
            startActivity(Intent(this@AITradingActivity, HistoryChatBotActivity::class.java))
            animateOpen()
        }
    }


    private fun animateOpen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
           this.overrideActivityTransition(
               OVERRIDE_TRANSITION_OPEN,
                R.anim.slide_animation_right,
                R.anim.slide_animation_left
            )
        } else {
            @Suppress("DEPRECATION")
            this.overridePendingTransition(
                R.anim.slide_animation_right,
                R.anim.slide_animation_left
            )
        }
    }

    fun RecyclerView.isAtBottom(): Boolean {
        val lm = layoutManager as? LinearLayoutManager ?: return true
        return lm.findLastVisibleItemPosition() >= adapter!!.itemCount - 2
    }
}
