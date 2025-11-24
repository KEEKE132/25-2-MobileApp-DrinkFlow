package com.mobileapp.drinkflow.domain.userProfile.dto

import com.mobileapp.drinkflow.domain.userProfile.ActivityLevel
import com.mobileapp.drinkflow.domain.userProfile.Gender
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class UserProfileUpdateRequest(
    @field:NotNull
    @field:Min(value = 1)
    val age: Int,

    @field:NotNull
    val gender: Gender,

    @field:NotNull
    @field:Min(value = 1)
    val height: Double,

    @field:NotNull
    @field:Min(value = 1)
    val weight: Double,

    @field:NotNull
    val activityLevel: ActivityLevel
)

