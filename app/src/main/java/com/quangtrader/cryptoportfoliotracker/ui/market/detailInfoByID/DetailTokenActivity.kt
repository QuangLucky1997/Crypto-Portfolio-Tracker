package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID

import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityDetailTokenBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailTokenActivity :
    BaseActivity<ActivityDetailTokenBinding>(ActivityDetailTokenBinding::inflate) {
    private val adapterDetailToken by lazy { AdapterDetailToken(this) }
    override fun onCreateView() {
        super.onCreateView()
        initView()
        handleData()

    }

    private fun handleData() {
        val logoData = intent.getStringExtra(Constants.EXTRA_LOGO_COIN)
        val symbolData = intent.getStringExtra(Constants.EXTRA_SYMBOL_COIN)
        binding.apply {
            Glide.with(this@DetailTokenActivity).load(logoData).into(imgToken)
            tokenName.text = symbolData
        }
    }

    private fun initView() {
        binding.viewPaperDetailCoinInfo.adapter = adapterDetailToken
        binding.viewPaperDetailCoinInfo.setUserInputEnabled(false)
        binding.tabMarket.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        TabLayoutMediator(binding.tabMarket, binding.viewPaperDetailCoinInfo) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.overview)
                }

                1 -> {
                    tab.text = getString(R.string.markets)
                }

                2 -> {
                    tab.text = getString(R.string.newDetail)
                }

                3 -> {
                    tab.text = getString(R.string.alert)
                }

            }
        }.attach()
    }
}