package com.quangtrader.cryptoportfoliotracker.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentSettingsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    override fun onViewCreated() {

    }
}