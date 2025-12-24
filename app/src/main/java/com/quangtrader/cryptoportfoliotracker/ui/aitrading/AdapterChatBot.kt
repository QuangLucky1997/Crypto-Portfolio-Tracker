package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.chatbot.ChatBotMessage
import javax.inject.Inject

class DiffCallback : DiffUtil.ItemCallback<ChatBotMessage>() {

    override fun areItemsTheSame(
        oldItem: ChatBotMessage,
        newItem: ChatBotMessage
    ): Boolean =
        oldItem === newItem

    override fun areContentsTheSame(
        oldItem: ChatBotMessage,
        newItem: ChatBotMessage
    ): Boolean =
        oldItem == newItem
}

class AdapterChatBot @Inject constructor() :
    ListAdapter<ChatBotMessage, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is ChatBotMessage.User -> VIEW_TYPE_USER
            is ChatBotMessage.Bot -> VIEW_TYPE_BOT
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == VIEW_TYPE_USER) {
            UserVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.custom_item_chat_user, parent, false)
            )
        } else {
            BotVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.custom_item_chat_bot, parent, false)
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val msg = getItem(position)) {
            is ChatBotMessage.User -> (holder as UserVH).bind(msg)
            is ChatBotMessage.Bot -> (holder as BotVH).bind(msg)
        }
    }

    class UserVH(view: View) : RecyclerView.ViewHolder(view) {
        private val tv = view.findViewById<TextView>(R.id.tvUserMessage)
        fun bind(msg: ChatBotMessage.User) {
            tv.text = msg.text
        }
    }

    class BotVH(view: View) : RecyclerView.ViewHolder(view) {
        private val tv = view.findViewById<TextView>(R.id.tvBotMessage)

        fun bind(msg: ChatBotMessage.Bot) {
            tv.text = when {
                msg.isLoading -> "Analyzing token..."
                msg.isError -> "⚠️ ${msg.text}"
                else -> msg.text
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_USER = 0
        private const val VIEW_TYPE_BOT = 1
    }
}
