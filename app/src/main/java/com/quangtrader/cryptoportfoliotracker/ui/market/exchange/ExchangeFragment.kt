package com.quangtrader.cryptoportfoliotracker.ui.market.exchange

import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentExchangeBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ExchangeFragment : BaseFragment<FragmentExchangeBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentExchangeBinding
        get() = FragmentExchangeBinding::inflate

    override fun onViewCreated() {

    }
}