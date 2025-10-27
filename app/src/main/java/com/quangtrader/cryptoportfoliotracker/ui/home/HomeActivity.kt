package com.quangtrader.cryptoportfoliotracker.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityHomeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.utils.formatMarketCap
import com.quangtrader.cryptoportfoliotracker.utils.formatPercent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.text.DecimalFormat

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    private val homeViewPaperAdapter by lazy { HomeViewPaperAdapter(this) }

    override fun onCreateView() {
        super.onCreateView()
        window.apply {
            navigationBarColor = resources.getColor(R.color.white, null)
            statusBarColor = resources.getColor(R.color.colorMain, null)
        }
        initData()

    }

    @SuppressLint("DefaultLocale")
    private fun initData() {
        binding.apply {
            binding.viewPagerHome.adapter = homeViewPaperAdapter
            binding.viewPagerHome.setUserInputEnabled(false)
            binding.bottomBar.setOnTabSelectListener(object :
                AnimatedBottomBar.OnTabSelectListener {
                override fun onTabSelected(
                    lastIndex: Int,
                    lastTab: AnimatedBottomBar.Tab?,


                    newIndex: Int,
                    newTab: AnimatedBottomBar.Tab
                ) {
                    binding.viewPagerHome.setCurrentItem(newIndex, true)
                }

                override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                    Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
                }
            })

            viewPagerHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomBar.selectTabAt(position, true)
                }
            })



        }
    }
}