package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID

import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentMarketBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketFragment : BaseFragment<FragmentMarketBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMarketBinding
        get() = FragmentMarketBinding::inflate

    override fun onViewCreated() {

    }
}