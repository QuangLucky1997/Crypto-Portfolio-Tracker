package com.quangtrader.cryptoportfoliotracker.ui.market.watchlists

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentWatchlistsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.coin.detailCoin.ChartTokenActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import com.quangtrader.cryptoportfoliotracker.utils.SwipeHelperRight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.graphics.toColorInt

@AndroidEntryPoint
class WatchListsFragment : BaseFragment<FragmentWatchlistsBinding>() {

    private val watchListsViewModel by viewModels<WatchListsViewModel>()

    @Inject
    lateinit var adapterWatchLists: AdapterWatchLists
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWatchlistsBinding
        get() = FragmentWatchlistsBinding::inflate

    override fun onViewCreated() {
        setData()
        setupSwipeHelper()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setData() {
        viewLifecycleOwner.lifecycleScope.launch {
            watchListsViewModel.getAllWatchListsCoin.collect {
                adapterWatchLists.data = it.toMutableList()
            }
        }
        binding.apply {
            rvWatchlist.adapter = adapterWatchLists
        }
        adapterWatchLists.subjectDetail = { data ->
            val intent = Intent(requireContext(), ChartTokenActivity::class.java)
            intent.putExtra(Constants.EXTRA_NAME_COIN, data.name)
            intent.putExtra(Constants.EXTRA_LOGO_COIN, data.logo)
            intent.putExtra(Constants.EXTRA_SYMBOL_COIN, data.symbol)
            intent.putExtra(Constants.EXTRA_PERCENT_24_H, data.percentChange24h)
            intent.putExtra(Constants.EXTRA_LOGO, data.logo)
            intent.putExtra(Constants.EXTRA_MARKET_CAP, data.marketCap)
            startActivity(intent)
        }
        adapterWatchLists.subjectDeleteWatchLists = { data ->
            viewLifecycleOwner.lifecycleScope.launch {
                watchListsViewModel.deleteFAVCoin(data)
                Toast.makeText(
                    requireActivity(),
                    "${data.name} remove from watchlist",
                    Toast.LENGTH_SHORT
                ).show()
            }
            adapterWatchLists.notifyDataSetChanged()
        }

    }

    private fun setupSwipeHelper() {
        val swipeHelperRight = object : SwipeHelperRight(requireContext(), binding.rvWatchlist) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder,
                underlayButtons: MutableList<UnderlayButton>
            ) {
                underlayButtons.add(
                    UnderlayButton(
                        context = requireContext(),
                        text = "Archive",
                        imageResId = R.drawable.ic_star,
                        "#4CAF50".toColorInt(),
                        clickListener = object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                adapterWatchLists.subjectDeleteWatchLists?.invoke(adapterWatchLists.data[pos])
                            }
                        }
                    ))

                underlayButtons.add(
                    UnderlayButton(
                        context = requireContext(),
                        text = "Delete",
                        imageResId = R.drawable.notification,
                        color = "#FE3B30".toColorInt(),
                        clickListener = object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {

                            }
                        }
                    ))
            }
        }


    }
}



