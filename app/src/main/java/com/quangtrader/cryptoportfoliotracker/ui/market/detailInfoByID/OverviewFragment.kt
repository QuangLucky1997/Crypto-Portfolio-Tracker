package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID

import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentOverviewBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : BaseFragment<FragmentOverviewBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOverviewBinding
        get() = FragmentOverviewBinding::inflate

    override fun onViewCreated() {

    }
}