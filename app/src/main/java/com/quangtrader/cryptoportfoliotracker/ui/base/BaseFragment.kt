package com.quangtrader.cryptoportfoliotracker.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

import java.util.Locale
import javax.inject.Inject

abstract class BaseFragment<V : ViewBinding> : Fragment() {

//    @Inject
//    lateinit var preferences: Preferences

    lateinit var binding: V

    abstract val _binding:(LayoutInflater, ViewGroup?, Boolean) -> V

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = _binding(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
//        app.prefs
//            .keyAppLanguage
//            .asObservable()
//            .autoDispose(scope())
//            .subscribe { language ->
//                initLanguage(language)
//            }
    }

    abstract fun onViewCreated()

    private fun initLanguage(language: String) {
        val locale = Locale(language)
        val config = resources.configuration.apply {
            setLocale(locale)
        }
        resources.updateConfiguration(config, resources.displayMetrics)
    }

}