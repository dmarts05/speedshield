package com.dmarts05.speedshield.data.model

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
)
