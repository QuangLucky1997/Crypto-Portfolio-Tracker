package com.quangtrader.cryptoportfoliotracker.ui.settings

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isGone
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.ThemeManager
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.common.utils.getAppVersion
import com.quangtrader.cryptoportfoliotracker.data.local.ThemeMode
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentSettingsBinding
import com.quangtrader.cryptoportfoliotracker.helper.Navigator
import com.quangtrader.cryptoportfoliotracker.helper.Preferences
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    @Inject
    lateinit var prefs: Preferences
    @Inject lateinit var navigator: Navigator
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

            textVersion.text = "Version ".plus(getAppVersion(requireActivity()).first)
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
            settingFeedBack.clicks {
               navigator.showSupport()
            }

//            settingRate.clicks {
//                showRateApp(requireActivity())
//            }
            settingPolicy.clicks {
                val url = Constants.POLICY_LINK
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            settingTerm.clicks {
                val url = Constants.TERM_OF_USE
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

//    fun showRateApp(activity: Activity) {
//        val reviewManager = ReviewManagerFactory.create(activity)
//        val request = reviewManager.requestReviewFlow()
//        request.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val reviewInfo = task.result
//                reviewManager.launchReviewFlow(activity, reviewInfo)
//            } else {
//                openPlayStore(activity)
//            }
//        }
//    }
}