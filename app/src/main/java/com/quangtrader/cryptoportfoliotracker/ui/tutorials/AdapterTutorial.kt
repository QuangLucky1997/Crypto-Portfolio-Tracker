package com.quangtrader.cryptoportfoliotracker.ui.tutorials

import android.view.LayoutInflater
import android.view.ViewGroup
import com.quangtrader.cryptoportfoliotracker.data.local.Tutorial
import com.quangtrader.cryptoportfoliotracker.databinding.CustomItemTutorialsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseAdapter
import javax.inject.Inject

class AdapterTutorial @Inject constructor() : BaseAdapter<Tutorial, CustomItemTutorialsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> CustomItemTutorialsBinding
        get() = CustomItemTutorialsBinding::inflate

    override fun bindItem(item: Tutorial, binding: CustomItemTutorialsBinding, position: Int) {
        binding.apply {
            imageView.setImageResource(item.imgResource)
            tvTitle.text = item.title
            tvSubtitle.text = item.subTitle
        }
    }
}