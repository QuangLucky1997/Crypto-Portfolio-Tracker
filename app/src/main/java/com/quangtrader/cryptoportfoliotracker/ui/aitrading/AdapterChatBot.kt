package com.quangtrader.cryptoportfoliotracker.ui.aitrading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.chatbot.ChatBotMessage
import javax.inject.Inject

class AdapterChatBot @Inject constructor() :
    ListAdapter<ChatBotMessage, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is ChatBotMessage.User -> VIEW_TYPE_USER
            is ChatBotMessage.Bot -> VIEW_TYPE_BOT
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_USER) {
            UserVH(inflater.inflate(R.layout.custom_item_chat_user, parent, false))
        } else {
            BotVH(inflater.inflate(R.layout.custom_item_chat_bot, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = getItem(position)
        when (holder) {
            is UserVH -> holder.bind(msg as ChatBotMessage.User)
            is BotVH -> holder.bind(msg as ChatBotMessage.Bot)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && holder is BotVH) {
            val newText = payloads[0] as String
            holder.updateStreamingText(newText)
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
      //  private val progressBar = view.findViewById<View>(R.id.pbLoading) // Giả định bạn có ProgressBar

        fun bind(msg: ChatBotMessage.Bot) {
           // progressBar?.visibility = if (msg.isLoading) View.VISIBLE else View.GONE

            tv.text = when {
                msg.isLoading && msg.text.isEmpty() -> "Analyzing token..."
                msg.isError -> "⚠️ ${msg.text}"
                else -> msg.text
            }

            // Đổi màu nếu lỗi
          //  tv.setTextColor(if (msg.isError) Color.RED else Color.BLACK)
        }

        fun updateStreamingText(newText: String) {
            tv.text = newText
         //   progressBar?.visibility = View.GONE
        }
    }

    // --- DiffCallback: Bộ não xử lý mượt mà ---

    class DiffCallback : DiffUtil.ItemCallback<ChatBotMessage>() {
        override fun areItemsTheSame(oldItem: ChatBotMessage, newItem: ChatBotMessage): Boolean {
            return when {
                oldItem is ChatBotMessage.User && newItem is ChatBotMessage.User ->
                    oldItem.timestamp == newItem.timestamp
                oldItem is ChatBotMessage.Bot && newItem is ChatBotMessage.Bot ->
                    oldItem.timestamp == newItem.timestamp
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: ChatBotMessage, newItem: ChatBotMessage): Boolean {
            return oldItem == newItem
        }
        override fun getChangePayload(oldItem: ChatBotMessage, newItem: ChatBotMessage): Any? {
            if (oldItem is ChatBotMessage.Bot && newItem is ChatBotMessage.Bot) {
                if (oldItem.text != newItem.text) return newItem.text
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }

    companion object {
        private const val VIEW_TYPE_USER = 0
        private const val VIEW_TYPE_BOT = 1
    }
}
