package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.newsInfo

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.data.remote.Article

import com.quangtrader.cryptoportfoliotracker.databinding.FragmentNewsByTypeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
import com.quangtrader.cryptoportfoliotracker.ui.news.ShowNewsActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import com.quangtrader.cryptoportfoliotracker.utils.formatDateTime
import com.quangtrader.cryptoportfoliotracker.utils.getRelativeTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsByTypeBinding>() {
    @Inject
    lateinit var adapterNewsByToken: AdapterNewsByToken
    private val newsByTokenViewModel by viewModels<NewsByTokenViewModel>()
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewsByTypeBinding
        get() = FragmentNewsByTypeBinding::inflate

    override fun onViewCreated() {
        val activity = requireActivity() as DetailTokenActivity
        val tokenID = activity.intent.getStringExtra(Constants.EXTRA_NAME_COIN)
        tokenID?.let {
          setData(tokenID.plus(" Token"))
        }
        adapterNewsByToken.subjectViewNew = { it->
            val intentDetail = Intent(requireContext(), ShowNewsActivity::class.java)
            intentDetail.putExtra(Constants.EXTRA_SOURCE_NEWS, it.source.name)
            intentDetail.putExtra(Constants.EXTRA_TIME_POST, formatDateTime(it.publishedAt))
            intentDetail.putExtra(Constants.EXTRA_LINK_NEWS, it.url)
            startActivity(intentDetail)
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setData(dataToken: String) {
        newsByTokenViewModel.getAllNewsByToken(dataToken)
        viewLifecycleOwner.lifecycleScope.launch {
            newsByTokenViewModel.dataNews.collect { coinData ->
                adapterNewsByToken.data = coinData.articles.toMutableList()
                binding.rvNewsFeedByType.adapter = adapterNewsByToken
            }
        }
    }
}