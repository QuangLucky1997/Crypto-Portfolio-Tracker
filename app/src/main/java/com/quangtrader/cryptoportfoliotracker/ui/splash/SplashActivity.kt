package com.quangtrader.cryptoportfoliotracker.ui.splash

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.common.utils.startMain
import com.quangtrader.cryptoportfoliotracker.common.utils.startTutorialActivity
import com.quangtrader.cryptoportfoliotracker.common.utils.tryOrNull
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.quangtrader.cryptoportfoliotracker.common.ads.GoogleMobileAdsConsentManager
import com.quangtrader.cryptoportfoliotracker.common.utils.isNetworkAvailable
import com.quangtrader.cryptoportfoliotracker.databinding.ActivitySplashBinding
import com.quangtrader.cryptoportfoliotracker.helper.Preferences
import com.quangtrader.cryptoportfoliotracker.inject.App.Companion.app
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    @Inject
    lateinit var preferences : Preferences
    private val subjectCreatedSplash: Subject<Unit> = PublishSubject.create()
    private val googleMobileAdsConsentManager by lazy {
        GoogleMobileAdsConsentManager.getInstance(
            this
        )
    }
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private val isDoTaskAfterSyncRemoteConfig = AtomicBoolean(false)
    override fun onCreateView() {
        super.onCreateView()
        initUISystem()
        subjectCreatedSplash
            .debounce(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(scope())
            .subscribe {
                googleMobileAdsConsentManager.gatherConsent(this) { consentError ->
                    if (consentError != null) {
                        Log.w(
                            "Main12345",
                            String.format("%s: %s", consentError.errorCode, consentError.message)
                        )
                    }

                    if (googleMobileAdsConsentManager.canRequestAds) {
                        initializeMobileAdsSdk()
                    }
                    syncRemoteConfig()
                }

                if (googleMobileAdsConsentManager.canRequestAds) {
                    initializeMobileAdsSdk()
                }
            }

        subjectCreatedSplash.onNext(Unit)
    }
    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        val testDeviceIds = arrayListOf(
            "07C522742A963A250A7322534F3CC425",
            "4C712C2697F26CA779CD641CE8FCA5DE",
            "2F5BB4121AB2058916253F4DFA5CBD13",
            "0602D9CBAC74F8618785506234D59782",
            "4A77CC7AB3AEB4A1E2BDAC2973CBF022"
        )

        if (BuildConfig.DEBUG) {
            testDeviceIds.add("29D59506E999419336DCBF6CE24F8F1F")
            testDeviceIds.add("A1667280A1723D491F3F0018AFB9636F")
        }

        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        MobileAds.initialize(this) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.d(
                    "Main1234567",
                    String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass,
                        status!!.description,
                        status.latency
                    )
                )
            }
        }
    }

    private fun syncRemoteConfig() {
        Firebase.remoteConfig.let { config ->
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
                setFetchTimeoutInSeconds(5)
            }
            config.setConfigSettingsAsync(configSettings)
            config.fetchAndActivate().addOnCompleteListener {
                if(it.isSuccessful) {
                    app.isShowAllAds = when {
                        else -> config.getBoolean("isShowAllAds")
                    }
                    app.isOpenSplash =
                        config.getBoolean("isOpenSplash").takeIf { app.isShowAllAds } ?: false
                    app.isBannerHome =
                        config.getBoolean("isBannerHome").takeIf { app.isShowAllAds } ?: false
                    app.isBannerShowNews =
                        config.getBoolean("isBannerShowNews").takeIf { app.isShowAllAds } ?: false
                    app.isFullScreenChanges =
                        config.getBoolean("isFullScreenChanges").takeIf { app.isShowAllAds } ?: false
                    app.isFullSplash =
                        config.getBoolean("isFullSplash").takeIf { app.isShowAllAds } ?: false
                    app.isOpenBackground =
                        config.getBoolean("isOpenBackground").takeIf { app.isShowAllAds } ?: false
                    app.isFullOrOpenSplash = config.getString("isFullOrOpenSplash")
                    app.fullAndOpenDisplayInterval = config.getLong("fullAndOpenDisplayInterval")
                    app.fullDisplayInterval = config.getLong("fullDisplayInterval")
                    app.fullScreenChangesInterval = config.getLong("fullScreenChangesInterval")
                    app.openDisplayInterval = config.getLong("openDisplayInterval")
                    Log.d("syncRemoteConfig", "syncRemoteConfig: ${app.isOpenBackground}")
                }
                doTaskAfterSyncRemoteConfig()
            }
        }
    }

    private fun loadAndShowFullAdmob(task: () -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {

            app.fullSplash.loadAd(this@SplashActivity) { isSuccess ->
                when {
                    isSuccess -> {
                        app.fullSplash.loadOrShowIfAvailable(
                            this@SplashActivity,
                            isLoadAgain = false
                        ) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                delay(app.delayFullDisplayInterval)
                                tryOrNull { task() }
                            }
                        }
                    }

                    else -> tryOrNull { task() }
                }
            }
        }
    }

    private fun loadAndShowOpenAdmob(task: () -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            app.openSplash.loadAd(this@SplashActivity) { isSuccess ->
                when {
                    isSuccess -> {
                        lifecycleScope.launch(Dispatchers.Main) {
                            delay(500L)
                            app.openSplash.loadOrShowIfAvailable(
                                this@SplashActivity,
                                isLoadAgain = false
                            ) {
                                tryOrNull { task() }
                            }
                        }
                    }

                    else -> tryOrNull { task() }
                }
            }
        }
    }
    private fun doTaskAfterSyncRemoteConfig() {
        val task = {
            when (preferences.isUpgraded.get()) {
                true -> startMain { finish() }
                else -> {
                    if (!preferences.isTutorial.get()) {
                        preferences.isTutorial.set(true)
                        startTutorialActivity { finish() }
                    } else {
                        startMain { finish() }
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            val isVip = preferences.isUpgraded.get()
            val isNetwork = isNetworkAvailable()

            if (!isVip && isNetwork) {
                if (app.isOpenSplash) {
                    binding.viewProgressAds.visibility = View.VISIBLE
                    loadAndShowOpenAdmob { task() }
                    return@launch
                }
                if (app.isFullSplash) {
                    binding.viewProgressAds.visibility = View.VISIBLE
                    loadAndShowFullAdmob { task() }
                    return@launch
                }
            }
            binding.viewProgressAds.visibility = View.GONE
            task()
        }
    }

}
