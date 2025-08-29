package com.quangtrader.cryptoportfoliotracker.ui.home

import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityHomeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import nl.joery.animatedbottombar.AnimatedBottomBar

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    private val homeViewPaperAdapter by lazy { HomeViewPaperAdapter(this) }
    override fun onCreateView() {
        super.onCreateView()
        initData()

    }

    private fun initData() {
        binding.apply {
            binding.viewPagerHome.adapter = homeViewPaperAdapter
            binding.viewPagerHome.setUserInputEnabled(false)
            binding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
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