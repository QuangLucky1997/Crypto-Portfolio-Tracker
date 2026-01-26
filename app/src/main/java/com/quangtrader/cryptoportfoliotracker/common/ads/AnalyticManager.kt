package com.quangtrader.cryptoportfoliotracker.common.ads

import androidx.annotation.Keep
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.quangtrader.cryptoportfoliotracker.inject.App
import timber.log.Timber
import java.util.Locale

object AnalyticManager {

    @Keep
    enum class AdjustEventType(val token: String) {
        PurchasedWeekEvent(token = "3a92sk"),
        PurchasedMonthEvent(token = "6yh5ix"),
        PurchasedYearEvent(token = "tvls89"),
        PurchasedLifeTimeEvent(token = "a9rnxy"),
        CancelEvent(token = "u9hnyv")
    }

    fun eventTrackingAdRevenue(
        valueMicros: Long,
        adFormat: String,
        abTestName: String,
        abTestVariant: String,
        currencyCode: String,
        adSourceName: String?
    ) {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(App.Companion.app)
        firebaseAnalytics.logEvent("ad_revenue",
            bundleOf(
                "valuemicros" to valueMicros,
                "value_micros" to valueMicros,
                "ad_format" to adFormat,
                "ab_test_name" to abTestName,
                "ab_test_variant" to abTestVariant
            )
        )
//        AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB).run {
//            this.setRevenue(valueMicros / 1000000.0, currencyCode)
//            this.adRevenueNetwork = adSourceName
//            Adjust.trackAdRevenue(this)
//        }
    }

    fun eventGDPR(success: Boolean) {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(App.Companion.app)
        firebaseAnalytics.logEvent("GDPR",
            bundleOf(
                "status" to if (success) "consent" else "not_consent",
                "country" to Locale.getDefault().country
            )
        )
    }

    fun eventCallGpt4() {
        Timber.Forest.tag("Main12345678").e("call_gpt_4")

        val firebaseAnalytics = FirebaseAnalytics.getInstance(App.Companion.app)
        firebaseAnalytics.logEvent("call_gpt_4", bundleOf())
    }

    fun eventCallGpt4Error() {
        Timber.Forest.tag("Main12345678").e("call_gpt_4_error")

        val firebaseAnalytics = FirebaseAnalytics.getInstance(App.Companion.app)
        firebaseAnalytics.logEvent("call_gpt_4_error", bundleOf())
    }

    fun eventCallGpt3() {
        Timber.Forest.tag("Main12345678").e("call_gpt_3")

        val firebaseAnalytics = FirebaseAnalytics.getInstance(App.Companion.app)
        firebaseAnalytics.logEvent("call_gpt_3", bundleOf())
    }

    fun eventCallGpt3Error() {
        Timber.Forest.tag("Main12345678").e("call_gpt_3_error")

        val firebaseAnalytics = FirebaseAnalytics.getInstance(App.Companion.app)
        firebaseAnalytics.logEvent("call_gpt_3_error", bundleOf())
    }

    fun eventCallCustomSearch() {
        Timber.Forest.tag("Main12345678").e("call_gpt_custom_search")

        val firebaseAnalytics = FirebaseAnalytics.getInstance(App.Companion.app)
        firebaseAnalytics.logEvent("call_gpt_custom_search", bundleOf())
    }

    fun eventCallCustomSearchError() {
        Timber.Forest.tag("Main12345678").e("call_gpt_custom_search_error")

        val firebaseAnalytics = FirebaseAnalytics.getInstance(App.Companion.app)
        firebaseAnalytics.logEvent("call_gpt_custom_search_error", bundleOf())
    }

}