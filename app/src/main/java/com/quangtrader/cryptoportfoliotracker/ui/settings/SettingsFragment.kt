package com.quangtrader.cryptoportfoliotracker.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentSettingsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    override fun onViewCreated() {
        initChangeView()
    }

    private fun initChangeView() {
        binding.apply {
            viewLight.clicks {
                cardDark.isGone = true
                cardSystem.isGone = true
                cardLight.isGone = false
            }
            viewDark.clicks {
                cardDark.isGone = false
                cardSystem.isGone = true
                cardLight.isGone = true
            }
            viewSystem.clicks {
                cardDark.isGone = true
                cardSystem.isGone = false
                cardLight.isGone = true
            }
        }
    }
}