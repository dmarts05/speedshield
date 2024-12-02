package com.dmarts05.speedshield.data.model

import com.google.gson.annotations.SerializedName

data class TokenResponseDto(
    @SerializedName("token")
    val token: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
)
