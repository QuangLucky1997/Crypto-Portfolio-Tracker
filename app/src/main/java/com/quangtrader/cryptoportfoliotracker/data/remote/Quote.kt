package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class Quote (
  @SerializedName("USD" ) var USD : USD? = USD()

)