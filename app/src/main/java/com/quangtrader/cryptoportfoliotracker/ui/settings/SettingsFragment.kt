package com.quangtrader.cryptoportfoliotracker.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.core.view.isGone
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.local.ThemeMode
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentSettingsBinding
import com.quangtrader.cryptoportfoliotracker.helper.Preferences
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.utils.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    @Inject
    lateinit var prefs: Preferences
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    override fun onViewCreated() {
        initChangeView()
        initSetView()
    }

    private fun initSetView() {
        binding.apply {
            when (prefs.themeMode.get()) {
                "LIGHT" -> {
                    cardDark.isGone = true
                    cardLight.isGone = false
                    cardLight.setCardBackgroundColor(resources.getColor(R.color.black))
                    textLight.setTextColor(resources.getColor(R.color.white))
                }

                "DARK" -> {
                    cardDark.isGone = false
                    cardLight.isGone = true
                    cardDark.setCardBackgroundColor(resources.getColor(R.color.white))
                    textDark.setTextColor(resources.getColor(R.color.black))
                }

            }
        }

    }

    private fun initChangeView() {
        binding.apply {
            viewLight.clicks {
                cardDark.isGone = true
                cardLight.isGone = false
                prefs.themeMode.set(ThemeMode.LIGHT.name)
                ThemeManager.applyTheme(ThemeMode.LIGHT)
                textLight.setTextColor(resources.getColor(R.color.black))
            }
            viewDark.clicks {
                cardDark.isGone = false
                cardLight.isGone = true
                prefs.themeMode.set(ThemeMode.DARK.name)
                ThemeManager.applyTheme(ThemeMode.DARK)
                textDark.setTextColor(resources.getColor(R.color.black))
            }
        }
    }
}