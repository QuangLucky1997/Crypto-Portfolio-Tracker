package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.chatbot.ChatBotMessage
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentAiTradingBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AITradingFragment : BaseFragment<FragmentAiTradingBinding>() {

    private val viewModel by viewModels<ChatBotViewModel>()

    @Inject
    lateinit var chatAdapter: AdapterChatBotGemini

    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAiTradingBinding
        get() = FragmentAiTradingBinding::inflate

    override fun onViewCreated() {
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messages.collectLatest { messages ->
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
                viewModel.sendMessage(text)
                edtMess.text.clear()
            }
        }

        imgBack.clicks {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        imgHistory.clicks(debounce = 500) {
            startActivity(Intent(requireContext(), HistoryChatBotActivity::class.java))
            animateOpen()
        }
    }


    private fun animateOpen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            activity?.overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_OPEN,
                R.anim.slide_animation_right,
                R.anim.slide_animation_left
            )
        } else {
            @Suppress("DEPRECATION")
            activity?.overridePendingTransition(
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
