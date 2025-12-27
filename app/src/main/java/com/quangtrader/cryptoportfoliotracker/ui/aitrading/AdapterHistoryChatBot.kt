package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.data.roommodel.HistoryChatBotEntity
import com.quangtrader.cryptoportfoliotracker.databinding.AdapterHistoryChatBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import javax.inject.Inject

class AdapterHistoryChatBot @Inject constructor() :
    BaseAdapter<HistoryChatBotEntity, AdapterHistoryChatBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> AdapterHistoryChatBinding
        get() = AdapterHistoryChatBinding::inflate

    override fun bindItem(
        item: HistoryChatBotEntity,
        binding: AdapterHistoryChatBinding,
        position: Int
    ) {

    }
}