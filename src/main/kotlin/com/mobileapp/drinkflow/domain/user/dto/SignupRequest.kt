package com.mobileapp.drinkflow.domain.user.dto

import jakarta.validation.constraints.NotBlank

data class SignupRequest(
    @NotBlank
    val username: String,
    @NotBlank
    val password: String,
    @NotBlank
    val name: String
)
