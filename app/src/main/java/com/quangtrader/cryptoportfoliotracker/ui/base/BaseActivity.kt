package com.quangtrader.cryptoportfoliotracker.ui.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.quangtrader.cryptoportfoliotracker.common.utils.getColorCompat
import com.quangtrader.cryptoportfoliotracker.common.utils.transparent
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.helper.lightNavigationBar
import com.quangtrader.cryptoportfoliotracker.helper.lightStatusBar
import java.util.Locale


open class BaseActivity<VB : ViewBinding>(
    val bindingInflater: (LayoutInflater) -> VB
) : AppCompatActivity() {
    val binding: VB by lazy { bindingInflater(layoutInflater) }
//    @Inject
//    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        preferences
//            .keyAppLanguage
//            .asObservable()
//            .autoDispose(scope())
//            .subscribe { language ->
//                initLanguage(language)
//            }
        setContentView(binding.root)
        onCreateView()
        window.statusBarColor = Color.WHITE
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.navigationBarColor = Color.WHITE


    }


    open fun onCreateView() {

    }
    private fun initLanguage(language: String) {
        val locale = Locale(language)
        val config = resources.configuration.apply {
            setLocale(locale)
        }
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun initUISystem(color: Int = R.color.white) {
        lightStatusBar()
        lightNavigationBar()
        transparent(true)
        window.apply {
            statusBarColor = getColorCompat(color)
            navigationBarColor = getColorCompat(color)
        }
    }




}
