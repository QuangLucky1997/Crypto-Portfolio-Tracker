package com.quangtrader.cryptoportfoliotracker.ui.market.watchlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.repository.ManagerDBRoomRepository
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchListsViewModel @Inject constructor(private val managerDBRoomRepository: ManagerDBRoomRepository) :
    ViewModel() {
    val getAllWatchListsCoin = managerDBRoomRepository.getAllFavCoin.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun toggleFav(coin: CoinFav, isFav: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = managerDBRoomRepository.checkIfExists(coin.name).first()
            if (count <= 0 && isFav) {
                managerDBRoomRepository.addFav(coin)
            } else {
                managerDBRoomRepository.updateFAVCoin(coin.name, isFav)
            }
        }
    }

    fun deleteFAVCoin(coin: CoinFav) {
        viewModelScope.launch(Dispatchers.IO) {
            managerDBRoomRepository.updateFAVCoin(coin.name, false)
        }
    }
}

