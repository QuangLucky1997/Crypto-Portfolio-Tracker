package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentAiTradingBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AITradingFragment : BaseFragment<FragmentAiTradingBinding>() {

    private val chatBotViewModel  by viewModels <ChatBotViewModel>()
    @Inject
    lateinit var adapterChatBot: AdapterChatBot

    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAiTradingBinding
        get() = FragmentAiTradingBinding::inflate

    override fun onViewCreated() {
         binding.apply {
             rvChatBot.adapter = adapterChatBot
             rvChatBot.setHasFixedSize(true)
             rvChatBot.itemAnimator = null
             observeMessages()
             btnSend.clicks {
                 val text = edtMess.text.toString().trim()
                 if (text.isNotEmpty()) {
                     chatBotViewModel.sendMessage(text)
                     edtMess.text.clear()
                 }
             }
         }
    }

    private fun observeMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                chatBotViewModel.messages.collectLatest { list ->
                    adapterChatBot.submitList(list) {
                        if (list.isNotEmpty()) {
                            binding.rvChatBot.scrollToPosition(list.lastIndex)
                        }
                    }
                }
            }
        }
    }
}