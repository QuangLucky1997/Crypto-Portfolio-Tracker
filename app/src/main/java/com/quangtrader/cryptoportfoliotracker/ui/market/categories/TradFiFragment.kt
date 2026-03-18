package com.quangtrader.cryptoportfoliotracker.ui.market.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCategoriesBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import www.sanju.motiontoast.MotionToast
import javax.inject.Inject

@AndroidEntryPoint
class TradFiFragment : BaseFragment<FragmentCategoriesBinding>() {
    private val tradFiViewModel by viewModels<TradFiViewModel>()

    @Inject
    lateinit var adapterTraFi: AdapterTradFi
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCategoriesBinding
        get() = FragmentCategoriesBinding::inflate

    override fun onViewCreated() {
        binding.rvTradFi.adapter = adapterTraFi
        loadData()
    }

    private fun loadData() {
        showLoading(true)
        tradFiViewModel.loadCategories()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tradFiViewModel.tradFiState.collect { state ->
                    when (state) {
                        is TradFiUIState.Loading -> {
                            showLoading(true)
                        }

                        is TradFiUIState.Success -> {
                            showLoading(false)
                            adapterTraFi.submitList(state.data)

                        }

                        is TradFiUIState.Error -> {
                            binding.loadingTradFi.setAnimation(R.raw.error404)
                            binding.loadingTradFi.playAnimation()
                            Timber.tag("TradFi").e(state.exception, "Load data failed")
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            if (show) {
                loadingTradFi.visibility = View.VISIBLE
                rvTradFi.visibility = View.INVISIBLE
                if (!loadingTradFi.isAnimating) {
                    loadingTradFi.playAnimation()
                }
            } else {
                if (loadingTradFi.isVisible) {
                    loadingTradFi.postDelayed({
                        loadingTradFi.cancelAnimation()
                        loadingTradFi.visibility = View.GONE
                        rvTradFi.visibility = View.VISIBLE
                    }, 800)
                }
            }
        }
    }
}