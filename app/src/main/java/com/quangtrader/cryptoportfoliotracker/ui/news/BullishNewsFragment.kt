package com.quangtrader.cryptoportfoliotracker.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentNewsByTypeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue
@AndroidEntryPoint
class BullishNewsFragment : BaseFragment<FragmentNewsByTypeBinding>() {
    private val newsViewModel by viewModels<NewsViewModel>()

    @Inject
    lateinit var adapterLoadNewsFeed: AdapterLoadNewsFeed

    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewsByTypeBinding
        get() = FragmentNewsByTypeBinding::inflate

    override fun onViewCreated() {
        setDataNewsHandpicked()
    }

    private fun setDataNewsHandpicked() {
        newsViewModel.getAllNewsByType("bullish")
        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.dataNews.collect { it ->
                adapterLoadNewsFeed.data = it.toMutableList()
                binding.rvNewsFeedByType.adapter = adapterLoadNewsFeed
            }

        }
    }
}