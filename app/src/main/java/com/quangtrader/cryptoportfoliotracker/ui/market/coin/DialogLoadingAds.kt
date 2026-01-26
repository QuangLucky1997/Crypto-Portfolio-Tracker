package com.quangtrader.cryptoportfoliotracker.ui.market.coin

import com.quangtrader.cryptoportfoliotracker.databinding.DialogLoadingAdsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.LsFullDialog
import javax.inject.Inject

class DialogLoadingAds @Inject constructor() : LsFullDialog<DialogLoadingAdsBinding>(DialogLoadingAdsBinding::inflate) {

    override fun onViewCreated() {
        isCancelable = false
    }
}