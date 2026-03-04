package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import android.app.Activity
import android.os.Build
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityHistoryChatbotBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HistoryChatBotActivity : BaseActivity<ActivityHistoryChatbotBinding>(
    ActivityHistoryChatbotBinding::inflate
) {
    private val chatBotViewModel by viewModels<ChatBotViewModel>()
    @Inject lateinit var adapterShowHistory  : AdapterHistoryChatBot
    override fun onCreateView() {
        super.onCreateView()
        handleClick()
        showHistoryChat()
        onBackPressedDispatcher.addCallback(this) {
            finish()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(
                    Activity.OVERRIDE_TRANSITION_CLOSE,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            } else {
                @Suppress("DEPRECATION")
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }
    }

    private fun handleClick() {
        binding.apply {
            imgBack.clicks {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun showHistoryChat(){
        lifecycleScope.launch {
            chatBotViewModel.getAllHistoryChat.collect { dataHistory ->
                //adapterShowHistory.data = dataHistory.toMutableList()
                adapterShowHistory.submitList(dataHistory)
                binding.rvHistoryBot.adapter = adapterShowHistory
            }
        }
    }
}