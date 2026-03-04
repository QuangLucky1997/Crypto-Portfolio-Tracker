package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.quangtrader.cryptoportfoliotracker.data.roommodel.HistoryChatBotEntity
import com.quangtrader.cryptoportfoliotracker.databinding.AdapterHistoryChatBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import com.quangtrader.cryptoportfoliotracker.common.utils.toDateTimeString
import javax.inject.Inject

class AdapterHistoryChatBot @Inject constructor() :
    BaseAdapter<HistoryChatBotEntity, AdapterHistoryChatBinding>(DiffCallbackHistoryChatBot()) {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> AdapterHistoryChatBinding
        get() = AdapterHistoryChatBinding::inflate

    override fun bindItem(
        item: HistoryChatBotEntity,
        binding: AdapterHistoryChatBinding,
        position: Int
    ) {
        binding.contentChatText.text = item.question
        binding.timeText.text = item.timestamp.toDateTimeString()

    }
    class DiffCallbackHistoryChatBot : DiffUtil.ItemCallback<HistoryChatBotEntity>() {
        override fun areItemsTheSame(oldItem: HistoryChatBotEntity, newItem: HistoryChatBotEntity): Boolean {
            return oldItem.question == newItem.question
        }

        override fun areContentsTheSame(oldItem: HistoryChatBotEntity, newItem: HistoryChatBotEntity): Boolean {
            return oldItem == newItem
        }
    }
}