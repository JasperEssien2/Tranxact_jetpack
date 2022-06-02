package com.crypto.tranxact.data.models

import com.google.gson.annotations.SerializedName

data class Exchange(
    @SerializedName("exchange_id") val id: String,
    @SerializedName("name") val name: String,
)
