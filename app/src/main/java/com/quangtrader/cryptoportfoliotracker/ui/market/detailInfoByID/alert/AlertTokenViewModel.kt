package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.alert

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.google.firebase.ktx.Firebase


@HiltViewModel
class AlertTokenViewModel : ViewModel() {
    private val firestore = Firebase.fir
}