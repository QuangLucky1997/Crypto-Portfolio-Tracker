package com.quangtrader.cryptoportfoliotracker.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityHomeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseNotificationHelper
import com.quangtrader.cryptoportfoliotracker.utils.openAppInfo
import dagger.hilt.android.AndroidEntryPoint
import nl.joery.animatedbottombar.AnimatedBottomBar

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    private val homeViewPaperAdapter by lazy { HomeViewPaperAdapter(this) }
    private lateinit var notificationHelper: BaseNotificationHelper

    override fun onCreateView() {
        super.onCreateView()
        window.apply {
            navigationBarColor = resources.getColor(R.color.white, null)
            statusBarColor = resources.getColor(R.color.colorMain, null)
        }
        initData()
        requestNotification()

    }

    private fun requestNotification() {
        notificationHelper = BaseNotificationHelper(this)
        if (!notificationHelper.isNotificationPermissionGranted()) {
            notificationHelper.requestNotificationPermission(this) { granted ->
                if (granted) {

                } else {
                    openAppNotificationSettings(this)
                }
            }
        }
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

    fun openAppNotificationSettings(context: Context) {
        try {
            val intent = Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            openAppInfo(context)
        }
    }
}