package com.quangtrader.cryptoportfoliotracker.ui.news

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.getRelativeTime
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentNewsByTypeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrendingNewsFragment : BaseFragment<FragmentNewsByTypeBinding>() {
    private val newsViewModel by viewModels<NewsViewModel>()

    @Inject
    lateinit var adapterLoadNewsFeed: AdapterLoadNewsFeed

    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewsByTypeBinding
        get() = FragmentNewsByTypeBinding::inflate

    override fun onViewCreated() {
        binding.rvNewsFeedByType.adapter = adapterLoadNewsFeed
        setupRecyclerView()
        observeViewModel()
        newsViewModel.getAllNewsByType("trending")
        openNews()
    }


    private fun setupRecyclerView() {
        binding.rvNewsFeedByType.adapter = adapterLoadNewsFeed
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is NewsViewUiState.Loading -> {
                            binding.animationLoading.isVisible = true
                        }

                        is NewsViewUiState.Success -> {
                            binding.animationLoading.isVisible = false
                            adapterLoadNewsFeed.data = uiState.data.toMutableList()
                           adapterLoadNewsFeed.notifyDataSetChanged()
                        }

                        is NewsViewUiState.Error -> {
                            binding.animationLoading.isVisible = false
                            val errorMsg =
                                uiState.exception.localizedMessage ?: "Can not load news data"
                            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                        }
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
            val intentDetail = Intent(requireContext(), ShowNewsActivity::class.java).apply {
                putExtra(Constants.EXTRA_SOURCE_NEWS, it.source)
                putExtra(Constants.EXTRA_TIME_POST, getRelativeTime(it.feedDate))
                putExtra(Constants.EXTRA_LINK_NEWS, it.link)
            }
            startActivity(intentDetail)
        }
    }




}