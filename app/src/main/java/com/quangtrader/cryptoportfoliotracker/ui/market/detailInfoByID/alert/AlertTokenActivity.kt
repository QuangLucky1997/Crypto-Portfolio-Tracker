package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.alert

import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityAlertTokenBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertTokenActivity :
    BaseActivity<ActivityAlertTokenBinding>(ActivityAlertTokenBinding::inflate) {
    private val adapterAlertToken by lazy { AdapterAlertToken(this) }
    override fun onCreateView() {
        super.onCreateView()
        initView()
        setData()
    }

    private fun setData() {
        val logoData = intent.getStringExtra(Constants.EXTRA_LOGO_COIN)
        val symbolData = intent.getStringExtra(Constants.EXTRA_SYMBOL_COIN)
        binding.apply {
            Glide.with(this@AlertTokenActivity).load(logoData).into(imgToken)
            tokenName.text = symbolData
        }
    }


    private fun initView() {
        binding.viewPaperAlertToken.adapter = adapterAlertToken
        binding.viewPaperAlertToken.setUserInputEnabled(false)
        binding.tabAlert.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        TabLayoutMediator(binding.tabAlert, binding.viewPaperAlertToken) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.alert_by_price)
                }

                1 -> {
                    tab.text = getString(R.string.alert_by_percent)
                }

            }
        }.attach()
    }
}