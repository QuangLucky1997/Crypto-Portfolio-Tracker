package com.quangtrader.cryptoportfoliotracker.ui.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root)