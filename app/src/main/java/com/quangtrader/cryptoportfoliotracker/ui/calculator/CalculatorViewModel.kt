package com.quangtrader.cryptoportfoliotracker.ui.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quangtrader.cryptoportfoliotracker.data.repository.ManagerDBRoomRepository
import com.quangtrader.cryptoportfoliotracker.data.roommodel.TokenTop100
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(private val managerDBRoomRepository: ManagerDBRoomRepository) :
    ViewModel() {

    val getAllTop100 = managerDBRoomRepository.allTokenTop100.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun saveTokenTop100(tokenTop100: TokenTop100) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = managerDBRoomRepository.checkIfExists(tokenTop100.name).first()
            if (count <= 0) {
                managerDBRoomRepository.saveTop100(tokenTop100)
            } else {
                //managerDBRoomRepository.updateFAVCoin(coin.name, isFav)
            }
        }
    }
}