package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.newsInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.remote.Article
import com.quangtrader.cryptoportfoliotracker.databinding.ItemNewsNormalBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import com.quangtrader.cryptoportfoliotracker.common.utils.formatDateTime
import javax.inject.Inject

class AdapterNewsByToken @Inject constructor() :
    BaseAdapter<Article, ItemNewsNormalBinding>(DiffCallbackNews()) {

    var subjectViewNew: ((Article) -> Unit)? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ItemNewsNormalBinding
        get() = ItemNewsNormalBinding::inflate

    override fun bindItem(
        item: Article,
        binding: ItemNewsNormalBinding,
        position: Int
    ) {
        binding.apply {
            Glide.with(root.context)
                .load(item.urlToImage)
                .into(imgNews)

            titleNews.text = item.title
            timePostNews.text = formatDateTime(item.publishedAt)
            sourceText.text = item.author ?: "Unknown"

            // Xử lý sự kiện click
            root.setOnClickListener {
                subjectViewNew?.invoke(item)
            }
        }
    }

    class DiffCallbackNews : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}