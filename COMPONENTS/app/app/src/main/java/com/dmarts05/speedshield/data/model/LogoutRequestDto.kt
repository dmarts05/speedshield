package com.dmarts05.speedshield.data.model

import com.google.gson.annotations.SerializedName

data class LogoutRequestDto(
    @SerializedName("refresh_token")
    val refreshToken: String,
)
