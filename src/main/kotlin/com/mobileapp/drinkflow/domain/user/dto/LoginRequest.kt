package com.mobileapp.drinkflow.domain.user.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @NotBlank
    val username: String,
    @NotBlank
    val password: String
)
