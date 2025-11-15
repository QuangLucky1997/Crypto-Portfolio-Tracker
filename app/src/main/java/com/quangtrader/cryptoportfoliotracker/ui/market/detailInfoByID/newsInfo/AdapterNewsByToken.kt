package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.newsInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.data.remote.Article
import com.quangtrader.cryptoportfoliotracker.databinding.ItemNewsNormalBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import com.quangtrader.cryptoportfoliotracker.utils.formatDateTime
import javax.inject.Inject

class AdapterNewsByToken @Inject constructor() : BaseAdapter<Article, ItemNewsNormalBinding>() {
    var subjectViewNew : ((Article) -> Unit)? = null
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ItemNewsNormalBinding
        get() = ItemNewsNormalBinding::inflate

    override fun bindItem(
        item: Article,
        binding: ItemNewsNormalBinding,
        position: Int
    ) {
        binding.apply {
            Glide.with(root).load(item.urlToImage).into(imgNews)
            titleNews.text = item.title
            timePostNews.text = formatDateTime(item.publishedAt)
            sourceText.text = item.author
            viewDetailNormal.clicks {
                subjectViewNew?.invoke(item)
            }


        }

    }
}