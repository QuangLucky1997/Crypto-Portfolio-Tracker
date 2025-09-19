package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.newsInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentNewsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNewsBinding
        get() = FragmentNewsBinding::inflate

    override fun onViewCreated() {

    }
}