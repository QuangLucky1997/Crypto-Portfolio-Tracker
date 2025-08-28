package com.quangtrader.cryptoportfoliotracker.ui.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDispose
import java.util.Locale
import javax.inject.Inject


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



}
