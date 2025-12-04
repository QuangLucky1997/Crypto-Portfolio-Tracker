package com.quangtrader.cryptoportfoliotracker.ui.calculator

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCalculatorProfitBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.coin.CoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CalculatorFragment : BaseFragment<FragmentCalculatorProfitBinding>() {
    private val coinViewModel by viewModels<CoinViewModel>()


    @Inject
    lateinit var adapterListTokenSpinner: AdapterListTokenSpinner
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCalculatorProfitBinding
        get() = FragmentCalculatorProfitBinding::inflate

    override fun onViewCreated() {
        initEvent()
        loadDataToken()
    }



    private fun loadDataToken() {
        binding.rvListToken.adapter = adapterListTokenSpinner
        binding.rvListToken.setHasFixedSize(true)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                coinViewModel.coins
                    .debounce(100)
                    .collect { list ->
                        if (list.isEmpty()) {
                            binding.rvListToken.visibility = View.GONE
                        } else {
                            binding.rvListToken.visibility = View.VISIBLE
                            adapterListTokenSpinner.data = list as MutableList<CoinUI>
                        }
                    }
            }
        }

        coinViewModel.loadCoins()
    }

    private fun initEvent() {
        binding.apply {
            edtSearchToken.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    rvListToken.visibility = View.VISIBLE
                } else {
                    rvListToken.visibility = View.GONE
                }
            }
            adapterListTokenSpinner.subjectChooseToken = {
                edtSearchToken.setText(it.symbol)
                Glide.with(requireActivity())
                    .load(it.logo)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            edtSearchToken.setCompoundDrawablesWithIntrinsicBounds(
                                resource, // LEFT
                                null,
                                null,
                                null
                            )
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
                rvListToken.visibility = View.GONE
            }
        }
    }
}