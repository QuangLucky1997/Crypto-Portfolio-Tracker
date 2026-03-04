package com.quangtrader.cryptoportfoliotracker.ui.base


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding



abstract class BaseAdapter<T : Any, V : ViewBinding>(
    callback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseViewHolder<V>>(callback) {

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> V

    abstract fun bindItem(item: T, binding: V, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V> {
        val binding = bindingInflater(LayoutInflater.from(parent.context), parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        val item = getItem(position) ?: return
        bindItem(item, holder.binding, position)
    }
}


