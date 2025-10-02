package com.quangtrader.cryptoportfoliotracker.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinUI
import com.quangtrader.cryptoportfoliotracker.data.remote.NewsResponse
import com.quangtrader.cryptoportfoliotracker.databinding.ItemNewsFeatureBinding
import com.quangtrader.cryptoportfoliotracker.databinding.ItemNewsNormalBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseMultiAdapter
import com.quangtrader.cryptoportfoliotracker.utils.getRelativeTime
import javax.inject.Inject

class AdapterLoadNewsFeed @Inject constructor() : BaseMultiAdapter<NewsResponse>() {
    var subjectDetailNew: ((NewsResponse) -> Unit)? = null

    companion object {
        private const val TYPE_FEATURED = 0
        private const val TYPE_NORMAL = 1
    }

    override fun getItemViewTypeForPosition(position: Int): Int {
        return if (position == 0) TYPE_FEATURED else TYPE_NORMAL
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ViewBinding {
        return if (viewType == TYPE_FEATURED) {
            ItemNewsFeatureBinding.inflate(inflater, parent, false)
        } else {
            ItemNewsNormalBinding.inflate(inflater, parent, false)
        }
    }

    override fun bindItem(
        item: NewsResponse,
        binding: ViewBinding,
        position: Int,
        viewType: Int
    ) {
        if (viewType == TYPE_FEATURED) {
            val featureBinding = binding as ItemNewsFeatureBinding
            Glide.with(featureBinding.root).load(item.imgUrl).into(featureBinding.imgNews)
            featureBinding.titleNews.text = item.title
            featureBinding.timePostNews.text = getRelativeTime(item.feedDate)
            featureBinding.sourceText.text = item.source
            featureBinding.viewDetailFeature.clicks {
                subjectDetailNew?.invoke(item)
            }
        } else {
            val normalBinding = binding as ItemNewsNormalBinding
            Glide.with(normalBinding.root).load(item.imgUrl).into(normalBinding.imgNews)
            normalBinding.titleNews.text = item.title
            normalBinding.timePostNews.text = getRelativeTime(item.feedDate)
            normalBinding.sourceText.text = item.source
            normalBinding.viewDetailNormal.clicks {
                subjectDetailNew?.invoke(item)
            }
        }
    }
}