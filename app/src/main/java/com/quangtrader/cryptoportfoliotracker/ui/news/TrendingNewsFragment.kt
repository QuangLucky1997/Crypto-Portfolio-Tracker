package com.quangtrader.cryptoportfoliotracker.ui.news

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentNewsByTypeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.getRelativeTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue
import androidx.core.view.isVisible

@AndroidEntryPoint
class TrendingNewsFragment : BaseFragment<FragmentNewsByTypeBinding>() {
    private val newsViewModel by viewModels<NewsViewModel>()

    @Inject
    lateinit var adapterLoadNewsFeed: AdapterLoadNewsFeed

    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewsByTypeBinding
        get() = FragmentNewsByTypeBinding::inflate

    override fun onViewCreated() {
        binding.rvNewsFeedByType.adapter = adapterLoadNewsFeed
        setDataNewsHandpicked()
        openNews()
    }



    private fun setDataNewsHandpicked() {
        newsViewModel.getAllNewsByType("trending")
        showLoading(true)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsViewModel.dataNews.collect { list ->
                    if (list.isNotEmpty()) {
                        adapterLoadNewsFeed.data = list.toMutableList()
                        delay(500)
                        showLoading(false)
                    }
                }
            }
        }
    }
    private fun showLoading(show: Boolean) {
        binding.apply {
            if (show) {
                animationLoading.visibility = View.VISIBLE
                rvNewsFeedByType.visibility = View.GONE
                animationLoading.post {
                    if (animationLoading.isVisible) {
                        animationLoading.playAnimation()
                    }
                }
            } else {
                animationLoading.visibility = View.GONE
                rvNewsFeedByType.visibility = View.VISIBLE
                animationLoading.cancelAnimation()
            }
        }
    }

    private fun openNews() {
        adapterLoadNewsFeed.subjectDetailNew = {
            val intentDetail = Intent(requireContext(), ShowNewsActivity::class.java)
            intentDetail.putExtra(Constants.EXTRA_SOURCE_NEWS, it.source)
            intentDetail.putExtra(Constants.EXTRA_TIME_POST, getRelativeTime(it.feedDate))
            intentDetail.putExtra(Constants.EXTRA_LINK_NEWS, it.link)
            startActivity(intentDetail)
        }
    }


}