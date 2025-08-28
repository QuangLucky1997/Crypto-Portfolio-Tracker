package com.quangtrader.cryptoportfoliotracker.ui.tutorials

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import authenticator.app.otp.authentication.fa.common.extentions.clickWithAnimationDebounce
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.local.Tutorial
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityTutorialsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class TutorialActivity : BaseActivity<ActivityTutorialsBinding>(ActivityTutorialsBinding::inflate) {
    @Inject
    lateinit var adapterTutorial: AdapterTutorial
    override fun onCreateView() {
        super.onCreateView()
        setData()
    }

    private fun setData() {
        window.statusBarColor = ContextCompat.getColor(this@TutorialActivity, R.color.white)
        binding.apply {
            viewPager2.adapter = adapterTutorial.apply {
                this.data = listData()
            }

            dotsIndicator.attachTo(viewPager2)
            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    textNext.text =
                        if (position == 2) getString(R.string.start) else getString(R.string.next)
                }
            })

            cardNext.clickWithAnimationDebounce {
                val currentPosition = viewPager2.currentItem
                if (currentPosition < (viewPager2.adapter?.itemCount ?: (0 - 1))) {
                    viewPager2.setCurrentItem(currentPosition + 1, true)
                }
                if (currentPosition == 2) {
                   // if(!preferences.isConfig3Days.get()){
                        lifecycleScope.launch {
                          //  tv3DayFree.isVisible = true
                            //   val animation = AnimationUtils.loadAnimation(this@OnBoardActivity, R.anim.animation_zoom)
                            //  tv3DayFree.startAnimation(animation)
                           // viewNext.isVisible = false
                            viewPager2.isVisible = false
                          //  preferences.isConfig3Days.set(true)
                            delay(1500)
                           // startIAPActivity(true)
                        }
                   // }
                  startActivity(Intent(this@TutorialActivity, HomeActivity::class.java))
                }
            }
        }
    }

    private fun listData(): MutableList<Tutorial> {
        val listTutorial = mutableListOf<Tutorial>()
        listTutorial.add(
            Tutorial(
                R.drawable.tutorial1,
                getString(R.string.takehold),
                getString(R.string.des_takehold)
            )
        )
        listTutorial.add(
            Tutorial(
                R.drawable.tutorial_2,
                getString(R.string.smart),
                getString(R.string.des_smart_trading)
            )
        )
        listTutorial.add(
            Tutorial(
                R.drawable.tutorial3_2,
                getString(R.string.invest),
                getString(R.string.des_invest)
            )
        )
        return listTutorial
    }

}