package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.newsInfo

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.formatDateTime
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentNewsByTypeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
import com.quangtrader.cryptoportfoliotracker.ui.news.ShowNewsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsByTypeBinding>() {
    @Inject
    lateinit var adapterNewsByToken: AdapterNewsByToken
    private val newsByTokenViewModel by viewModels<NewsByTokenViewModel>()
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewsByTypeBinding
        get() = FragmentNewsByTypeBinding::inflate

    override fun onViewCreated() {
        val activity = requireActivity() as DetailTokenActivity
        binding.rvNewsFeedByType.adapter = adapterNewsByToken
        val tokenID = activity.intent.getStringExtra(Constants.EXTRA_NAME_COIN)
        tokenID?.let {
            setData(tokenID.plus(" Token"))
        }
        handleClick()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setData(dataToken: String) {
        showLoading(true)
        newsByTokenViewModel.getAllNewsByToken(dataToken)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsByTokenViewModel.dataNews.collect { newsData ->
                    when (newsData) {
                        is NewsUiState.Loading -> {
                            showLoading(true)
                        }

                        is NewsUiState.Success -> {
                            showLoading(false)
                            adapterNewsByToken.submitList(newsData.data.articles)
                        }
                        else -> {
                            binding.animationLoading.setAnimation(R.raw.error404)
                            binding.animationLoading.playAnimation()
                        }
                    }
                }
            }
        }
    }

    private fun handleClick() {
        adapterNewsByToken.subjectViewNew = { it ->
            val intentDetail = Intent(requireContext(), ShowNewsActivity::class.java).apply {
                putExtra(Constants.EXTRA_SOURCE_NEWS, it.source.name)
                putExtra(Constants.EXTRA_TIME_POST, formatDateTime(it.publishedAt))
                putExtra(Constants.EXTRA_LINK_NEWS, it.url)
            }
            startActivity(intentDetail)
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
}