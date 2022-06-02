package com.crypto.tranxact.data.models

import com.google.gson.annotations.SerializedName

data class Asset(
    @SerializedName("asset_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("price_usd") val priceUSD: Double,
    @SerializedName("type_is_crypto") val isCrypto: Int,
)
