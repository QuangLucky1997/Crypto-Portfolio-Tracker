package com.quangtrader.cryptoportfoliotracker.ui.settings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.local.FeedBack
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinItem
import com.quangtrader.cryptoportfoliotracker.databinding.CustomFeedbackBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import javax.inject.Inject

class FeedbackAdapter @Inject constructor() :
    BaseAdapter<FeedBack, CustomFeedbackBinding>(DiffCallbackFeedback()) {

    var subjectFeedbackItemPosition: ((FeedBack, ArrayList<FeedBack>) -> Unit)? = null
    private val selectedList = arrayListOf<FeedBack>()
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomFeedbackBinding
        get() = CustomFeedbackBinding::inflate

    @SuppressLint("ResourceAsColor")
    override fun bindItem(
        item: FeedBack,
        binding: CustomFeedbackBinding,
        position: Int
    ) {
        binding.apply {
            textItemFeedback.text = item.title
            backgroundItemFeedback.setBackgroundResource(if (item.isChoose) R.color.green else R.color.white)
            textItemFeedback.setTextColor(if (item.isChoose) R.color.white else R.color.black)
            cardType.clicks {
                item.isChoose = !item.isChoose
                if (item.isChoose) {
                    selectedList.add(item)
                } else {
                    selectedList.remove(item)
                }
                notifyItemChanged(position)
                subjectFeedbackItemPosition?.invoke(item, ArrayList(selectedList))


            }

        }
    }
    class DiffCallbackFeedback : DiffUtil.ItemCallback<FeedBack>() {
        override fun areItemsTheSame(oldItem: FeedBack, newItem: FeedBack): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: FeedBack, newItem: FeedBack): Boolean {
            return oldItem == newItem
        }
    }

}
