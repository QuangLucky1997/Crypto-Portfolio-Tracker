package com.quangtrader.cryptoportfoliotracker.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentNewsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>() {
    private val adapterNews by lazy { AdapterNews(requireActivity()) }
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewsBinding
        get() = FragmentNewsBinding::inflate

    override fun onViewCreated() {
       initView()
    }

    private fun initView() {
        binding.viewPaperNews.adapter = adapterNews
        binding.viewPaperNews.setUserInputEnabled(false)
        binding.tabNews.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        TabLayoutMediator(binding.tabNews, binding.viewPaperNews) { tab, position ->
            when (position) {

                0 -> {
                    tab.text = getString(R.string.trending_news)
                }
                1 -> {
                    tab.text = getString(R.string.bullish_news)
                }
                2-> {
                    tab.text = getString(R.string.latest_news)
                }
                3 -> {
                    tab.text = getString(R.string.bearish_news)
                }
            }
        }.attach()
    }
}