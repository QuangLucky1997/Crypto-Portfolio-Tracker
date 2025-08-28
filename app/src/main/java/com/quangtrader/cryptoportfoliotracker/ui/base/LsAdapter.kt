package com.quangtrader.cryptoportfoliotracker.ui.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class LsAdapter<T, VB : ViewBinding>(
    val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val isNotifyDataChanged: Boolean = true
) : RecyclerView.Adapter<LsViewHolder<VB>>() {

    var data: List<T> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            if (field === value) return
            field = value
            if (isNotifyDataChanged) notifyDataSetChanged()
        }

    abstract fun bindItem(item: T, binding: VB, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LsViewHolder<VB> {
        return LsViewHolder(bindingInflater(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: LsViewHolder<VB>, position: Int) {
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