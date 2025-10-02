package com.quangtrader.cryptoportfoliotracker.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseMultiAdapter<T> : RecyclerView.Adapter<BaseViewHolder<out ViewBinding>>() {

    var data: MutableList<T> = mutableListOf()

    abstract fun getItemViewTypeForPosition(position: Int): Int

    abstract fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ViewBinding

    abstract fun bindItem(
        item: T,
        binding: ViewBinding,
        position: Int,
        viewType: Int
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out ViewBinding> {
        val binding = createBinding(LayoutInflater.from(parent.context), parent, viewType)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<out ViewBinding>, position: Int) {
        val item = data[position]
        bindItem(item, holder.binding, position, getItemViewType(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItemViewTypeForPosition(position)
    }

    override fun getItemCount(): Int = data.size
}