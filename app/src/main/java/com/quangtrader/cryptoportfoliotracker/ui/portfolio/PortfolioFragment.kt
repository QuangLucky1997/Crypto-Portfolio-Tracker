package com.quangtrader.cryptoportfoliotracker.ui.portfolio

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentPortfolioBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PortfolioFragment : BaseFragment<FragmentPortfolioBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPortfolioBinding
        get() = FragmentPortfolioBinding::inflate

    override fun onViewCreated() {
        initData()
    }

    private fun initData() {
        binding.apply {
            cardAddTransaction.clicks {
                val intentAddTransaction =
                    Intent(requireContext(), AddTransactionActivity::class.java)
                startActivity(intentAddTransaction)
            }
        }
    }
}