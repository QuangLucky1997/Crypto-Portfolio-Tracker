package com.quangtrader.cryptoportfoliotracker.ui.portfolio

import androidx.lifecycle.ViewModel
import com.quangtrader.cryptoportfoliotracker.data.repository.ManagerDBRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val managerDBRoomRepository: ManagerDBRoomRepository
) : ViewModel() {

//    fun getCoinsByPortfolio(portfolioId: Long) =
//        managerDBRoomRepository.getCoinsByPortfolio(portfolioId)


}