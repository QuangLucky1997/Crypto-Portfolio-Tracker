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

class AdapterChatBotGemini @Inject constructor() :
    ListAdapter<ChatBotMessage, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is ChatBotMessage.User -> VIEW_TYPE_USER
            is ChatBotMessage.Bot -> VIEW_TYPE_BOT
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER ->
                UserVH(inflater.inflate(R.layout.custom_item_chat_user, parent, false))
            else ->
                BotVH(inflater.inflate(R.layout.custom_item_chat_bot, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserVH -> holder.bind(getItem(position) as ChatBotMessage.User)
            is BotVH -> holder.bind(getItem(position) as ChatBotMessage.Bot)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && holder is BotVH) {
            holder.bind(getItem(position) as ChatBotMessage.Bot)
        } else {
            super.onBindViewHolder(holder, position, payloads)
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
        private val lottie =
            view.findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.lottieAILoading)

        fun bind(msg: ChatBotMessage.Bot) {
            when {
                msg.isLoading -> showLoading()
                msg.isError -> showText("⚠️ ${msg.text}")
                else -> showText(msg.text)
            }
        }

        private fun showLoading() {
            tv.visibility = View.GONE
            lottie.visibility = View.VISIBLE
            lottie.playAnimation()
        }

        private fun showText(text: String) {
            lottie.cancelAnimation()
            lottie.visibility = View.GONE
            tv.visibility = View.VISIBLE
            tv.text = text
        }
    }



    class DiffCallback : DiffUtil.ItemCallback<ChatBotMessage>() {

        override fun areItemsTheSame(oldItem: ChatBotMessage, newItem: ChatBotMessage): Boolean =
            oldItem.timestamp == newItem.timestamp

        override fun areContentsTheSame(oldItem: ChatBotMessage, newItem: ChatBotMessage): Boolean =
            oldItem == newItem

        override fun getChangePayload(oldItem: ChatBotMessage, newItem: ChatBotMessage): Any? {
            return if (oldItem is ChatBotMessage.Bot && newItem is ChatBotMessage.Bot) {
                if (
                    oldItem.text != newItem.text ||
                    oldItem.isLoading != newItem.isLoading ||
                    oldItem.isError != newItem.isError
                ) Unit else null
            } else null
        }
    }

    companion object {
        private const val VIEW_TYPE_USER = 0
        private const val VIEW_TYPE_BOT = 1
    }
}