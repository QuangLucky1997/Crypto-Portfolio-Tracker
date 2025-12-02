package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.alert

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentAlertByPriceBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.utils.Constants

class AlertByPriceFragment : BaseFragment<FragmentAlertByPriceBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAlertByPriceBinding
        get() = FragmentAlertByPriceBinding::inflate

    override fun onViewCreated() {
    }

    private fun handleEvent() {
        binding.apply {
            edtValueToken.requestFocus()
            edtValueToken.post {
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(edtValueToken, InputMethodManager.SHOW_IMPLICIT)
            }
            edtValueToken.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        viewSave.setBackgroundColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.yellowMax
                            )
                        )
                        cardSave.alpha = 1F
                        cardSave.isClickable = true

                    } else {
                        viewSave.setBackgroundColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.white
                            )
                        )
                        cardSave.alpha = 0.5F
                        cardSave.isClickable = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            val symbolData = requireActivity().intent.getStringExtra(Constants.EXTRA_SYMBOL_COIN)
            tokenText.text = symbolData.plus("/USDT")
        }
    }

    override fun onResume() {
        super.onResume()
        handleEvent()
    }
}