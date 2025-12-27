package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import android.app.Activity
import android.os.Build
import androidx.activity.addCallback
import androidx.activity.viewModels
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityHistoryChatbotBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryChatBotActivity : BaseActivity<ActivityHistoryChatbotBinding>(
    ActivityHistoryChatbotBinding::inflate
) {
    private val chatBotViewModel by viewModels<ChatBotViewModel>()
    override fun onCreateView() {
        super.onCreateView()
        handleClick()
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

    }
}