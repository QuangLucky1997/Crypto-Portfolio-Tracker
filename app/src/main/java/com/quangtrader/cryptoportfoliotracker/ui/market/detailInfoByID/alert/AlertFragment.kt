package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.alert

import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentAlertBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertFragment : BaseFragment<FragmentAlertBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAlertBinding
        get() = FragmentAlertBinding::inflate

    override fun onViewCreated() {

    }
}