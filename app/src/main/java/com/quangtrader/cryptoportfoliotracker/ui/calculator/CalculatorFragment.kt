package com.quangtrader.cryptoportfoliotracker.ui.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCalculatorProfitBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculatorFragment : BaseFragment<FragmentCalculatorProfitBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCalculatorProfitBinding
        get() = FragmentCalculatorProfitBinding::inflate

    override fun onViewCreated() {
        initEvent()
    }

    private fun initEvent() {
        binding.apply {
            cardCustomSearch.clicks {

            }
        }
    }
}