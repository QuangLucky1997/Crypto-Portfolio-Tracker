package com.quangtrader.cryptoportfoliotracker.ui.settings

import android.graphics.Color
import android.os.Bundle
import androidx.core.view.isGone
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.local.FeedBack
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityFeedbackBinding
import com.quangtrader.cryptoportfoliotracker.helper.lightStatusBar
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.common.utils.sendEmailViaGmail

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeedbackActivity : BaseActivity<ActivityFeedbackBinding>(ActivityFeedbackBinding::inflate) {
    @Inject
    lateinit var adapterFeedback: FeedbackAdapter
    private var listChooseData = arrayListOf<String>()
    private var dataFeedback = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
        window.statusBarColor = Color.WHITE
        initSetData()
        initHandleSubmit()
    }

    private fun initHandleSubmit() {
        binding.apply {
            adapterFeedback.subjectFeedbackItemPosition = { item, selectedList ->
                listChooseData.clear()
                listChooseData.addAll(selectedList.map {
                    it.title
                })
                cardSubmit.isGone = listChooseData.isEmpty()
            }
            cardSubmit.clicks {
                listChooseData.map {
                    dataFeedback = dataFeedback + it + "\n"
                }
                sendEmailViaGmail(
                    this@FeedbackActivity,
                    "Voidmaink39c@gmail.com",
                    "Feedback CRYPTO_TRACKER",
                    dataFeedback
                )
            }
            backButton.clicks {
                finish()
            }
        }

    }

    private fun initSetData() {
        val feedBacks: MutableList<FeedBack> = mutableListOf()
        feedBacks.add(FeedBack(getString(R.string.data)))
        feedBacks.add(FeedBack(getString(R.string.lag)))
        feedBacks.add(FeedBack(getString(R.string.UI)))
        adapterFeedback.data = feedBacks
        binding.rvChooseType.adapter = adapterFeedback
    }
}