package com.quangtrader.cryptoportfoliotracker.ui.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.data.local.CoinTransaction
import com.quangtrader.cryptoportfoliotracker.databinding.CustomItemHeaderBinding
import com.quangtrader.cryptoportfoliotracker.databinding.CustomListTokenTransactionBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseMultiAdapter
import javax.inject.Inject

class AdapterAddTransaction @Inject constructor() : BaseMultiAdapter<CoinTransaction>() {
    var subjectTransaction: (( CoinTransaction.CoinItem) -> Unit)? = null

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_COIN = 1
    }

    override fun getItemViewTypeForPosition(position: Int): Int {
        return when (data[position]) {
            is CoinTransaction.Header -> TYPE_HEADER
            is CoinTransaction.CoinItem -> TYPE_COIN
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ViewBinding {
        return when (viewType) {
            TYPE_HEADER -> CustomItemHeaderBinding.inflate(inflater, parent, false)
            TYPE_COIN -> CustomListTokenTransactionBinding.inflate(inflater, parent, false)
            else -> throw IllegalArgumentException("Invalid viewType: $viewType")
        }
    }

    override fun bindItem(
        item: CoinTransaction,
        binding: ViewBinding,
        position: Int,
        viewType: Int
    ) {
        when (viewType) {
            TYPE_HEADER -> {
                val header = item as CoinTransaction.Header
                val headerBinding = binding as CustomItemHeaderBinding
                headerBinding.textHeader.text = header.title
            }

            TYPE_COIN -> {
                val coin = item as CoinTransaction.CoinItem
                val coinBinding = binding as CustomListTokenTransactionBinding
                coinBinding.fullTokenName.text = coin.name
                coinBinding.textSymbolToken.text = coin.symbol

                Glide.with(coinBinding.root)
                    .load(coin.logo)
                    .into(coinBinding.logoToken)
                coinBinding.viewToken.clicks {
                    subjectTransaction?.invoke(coin)
                }
            }
        }
    }
}