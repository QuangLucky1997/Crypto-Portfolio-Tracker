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
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.roommodel.HistoryChatBotEntity
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentAiTradingBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.utils.toDateTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.java

@AndroidEntryPoint
class AITradingFragment : BaseFragment<FragmentAiTradingBinding>() {

    private val chatBotViewModel by viewModels<ChatBotViewModel>()

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
            handleClick()
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

    private fun handleClick() {
        binding.apply {
            btnSend.clicks {
                val text = edtMess.text.toString().trim()
                if (text.isNotEmpty()) {
                    chatBotViewModel.sendMessage(text)
                    val chatBotModel = HistoryChatBotEntity(0,text,System.currentTimeMillis())
                    chatBotViewModel.saveHistoryChat(chatBotModel)
                    edtMess.text.clear()
                }
            }
            imgHistory.clicks {
                val intent = Intent(requireActivity(), HistoryChatBotActivity::class.java)
                startActivity(intent)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                  activity?.overrideActivityTransition(
                        Activity.OVERRIDE_TRANSITION_OPEN,
                        R.anim.slide_animation_right,
                        R.anim.slide_animation_left
                    )
                } else {
                    @Suppress("DEPRECATION")
                    activity?.overridePendingTransition(R.anim.slide_animation_right, R.anim.slide_animation_left)
                }
            }
        }
    }
}