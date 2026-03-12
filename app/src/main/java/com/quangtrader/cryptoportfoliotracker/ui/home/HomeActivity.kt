package com.quangtrader.cryptoportfoliotracker.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.quangtrader.cryptoportfoliotracker.common.utils.tryOrNull
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.common.utils.isNetworkAvailable
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityHomeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseNotificationHelper
import com.quangtrader.cryptoportfoliotracker.common.utils.openAppInfo
import com.quangtrader.cryptoportfoliotracker.helper.Preferences
import com.quangtrader.cryptoportfoliotracker.inject.App.Companion.app
import com.quangtrader.cryptoportfoliotracker.ui.aitrading.AITradingActivity
import dagger.hilt.android.AndroidEntryPoint
import nl.joery.animatedbottombar.AnimatedBottomBar
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    private val homeViewPaperAdapter by lazy { HomeViewPaperAdapter(this) }
    @Inject
    lateinit var preferences : Preferences
    private lateinit var notificationHelper: BaseNotificationHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            navigationBarColor = resources.getColor(R.color.white, null)
            statusBarColor = resources.getColor(R.color.white, null)
        }
        initData()
        requestNotification()
        handleChatBot()
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
    private fun loadBannerAdmob() {
        tryOrNull {
            binding.viewAd.isVisible = true
            binding.viewBanner.isVisible = true
            binding.holderLoading.root.isVisible = true
            binding.holderLoading.mShimmerFrameLayout.startShimmer()
            app.bannerHome.loadAdMulti(this, binding.viewGroupAd) { isSuccess ->
                when {
                    isSuccess -> {
                        binding.holderLoading.root.isVisible = false
                        binding.holderLoading.mShimmerFrameLayout.stopShimmer()
                    }

                    else -> {
                        binding.viewAd.isVisible = false
                    }
                }
            }
        }
    }

    private fun handleChatBot(){
        binding.chatBotAnimation.clicks {
           startActivity(Intent(this, AITradingActivity::class.java))
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
                    if (position == 1) {
                        binding.chatBotAnimation.visibility = View.GONE
                    } else {
                        binding.chatBotAnimation.visibility = View.VISIBLE
                    }
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

    override fun onResume() {
        super.onResume()
        when {
            !preferences.isUpgraded.get() && isNetworkAvailable() && app.isBannerHome && !app.isMaxClickAdsTotal() -> {
                loadBannerAdmob()
            }

            else -> binding.viewAd.isVisible = false
        }

    }

    fun setLottieVisibility(isVisible: Boolean) {
        binding.chatBotAnimation.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}