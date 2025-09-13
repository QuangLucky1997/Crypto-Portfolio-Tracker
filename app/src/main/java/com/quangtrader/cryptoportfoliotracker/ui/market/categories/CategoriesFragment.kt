package com.quangtrader.cryptoportfoliotracker.ui.market.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentCategoriesBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding>() {
    private val categoriesViewModel by viewModels<CategoriesViewModel>()

    @Inject
    lateinit var adapterCategories: AdapterCategories
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCategoriesBinding
        get() = FragmentCategoriesBinding::inflate

    override fun onViewCreated() {
        loadData()
    }

    private fun loadData() {
        categoriesViewModel.loadCategories()
        viewLifecycleOwner.lifecycleScope.launch {
            categoriesViewModel.categories.collect { coinData ->
                val limitedData = coinData.take(30)
                adapterCategories.data.clear()
                adapterCategories.data.addAll(limitedData)
                binding.rvCategories.adapter = adapterCategories
            }
        }
    }
}