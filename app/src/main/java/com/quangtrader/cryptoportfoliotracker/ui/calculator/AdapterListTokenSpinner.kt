package com.quangtrader.cryptoportfoliotracker.ui.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.data.local.Tutorial
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
import com.quangtrader.cryptoportfoliotracker.databinding.CustomListSearchTokenBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import javax.inject.Inject

class AdapterListTokenSpinner @Inject constructor() :
    BaseAdapter<CoinUI, CustomListSearchTokenBinding>(DiffCallbackListTokenSniper()) {
    var subjectChooseToken: ((CoinUI) -> Unit)? = null
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomListSearchTokenBinding
        get() = CustomListSearchTokenBinding::inflate

    override fun bindItem(
        item: CoinUI,
        binding: CustomListSearchTokenBinding,
        position: Int
    ) {
        binding.apply {
            nameToken.text = item.name
            Glide.with(root).load(item.logo).into(iconToken)
            viewChooseToken.clicks {
                subjectChooseToken?.invoke(item)
            }
        }
    }
    class DiffCallbackListTokenSniper : DiffUtil.ItemCallback<CoinUI>() {
        override fun areItemsTheSame(oldItem: CoinUI, newItem: CoinUI): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CoinUI, newItem: CoinUI): Boolean {
            return oldItem == newItem
        }
    }
}