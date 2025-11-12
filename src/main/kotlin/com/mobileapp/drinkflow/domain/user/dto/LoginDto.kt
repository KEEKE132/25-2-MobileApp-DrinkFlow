package com.mobileapp.drinkflow.domain.user.dto

import com.mobileapp.drinkflow.core.jwt.TokenPair

data class LoginDto(
    val user: UserResponse,
    val tokens: TokenPair
)
