package com.quangtrader.cryptoportfoliotracker.ui.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentPortfolioBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortfolioFragment: BaseFragment<FragmentPortfolioBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPortfolioBinding
    get() = FragmentPortfolioBinding::inflate

    override fun onViewCreated() {

    }
}