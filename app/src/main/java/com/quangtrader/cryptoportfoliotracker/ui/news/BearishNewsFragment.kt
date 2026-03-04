package com.quangtrader.cryptoportfoliotracker.ui.news

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.getRelativeTime
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentNewsByTypeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BearishNewsFragment : BaseFragment<FragmentNewsByTypeBinding>() {
    private val newsViewModel by viewModels<NewsViewModel>()

    @Inject
    lateinit var adapterLoadNewsFeed: AdapterLoadNewsFeed

    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewsByTypeBinding
        get() = FragmentNewsByTypeBinding::inflate

    override fun onViewCreated() {
        setupRecyclerView()
        observeViewModel()
        newsViewModel.getAllNewsByType("bearish")
        openLink()
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

    private fun openLink() {
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