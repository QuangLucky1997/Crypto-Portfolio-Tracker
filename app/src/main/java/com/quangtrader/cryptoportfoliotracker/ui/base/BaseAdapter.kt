package com.quangtrader.cryptoportfoliotracker.ui.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, V : ViewBinding>() : RecyclerView.Adapter<BaseViewHolder<V>>() {

    var data: MutableList<T> = ArrayList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            if (field == value) return
            field = value
            notifyDataSetChanged()
        }

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> V

    abstract fun bindItem(item: T, binding: V, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V> {
        return BaseViewHolder(bindingInflater(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        val item = getItem(position) ?: return
        bindItem(item, holder.binding, position)
    }

    fun getItem(position: Int): T? {
        return data.getOrNull(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}