package com.quangtrader.cryptoportfoliotracker.data.repository
import javax.inject.Inject
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

data class PriceAlert(
    val userId: String,
    val symbol: String,
    val targetPrice: Double,
    val direction: String,
    val fcmToken: String,
    val active: Boolean = true,
    val createdAt: Any = FieldValue.serverTimestamp()
)

class AlertRepository @Inject constructor(private val firestore: FirebaseFirestore) {

}