package com.mobileapp.drinkflow.core.jwt

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)
