package com.mobileapp.drinkflow.domain.userProfile.dto

import com.mobileapp.drinkflow.domain.userProfile.ActivityLevel
import com.mobileapp.drinkflow.domain.userProfile.Gender
import jakarta.validation.constraints.Min

data class UserProfileUpdateRequest(
    @field:Min(value = 1)
    val age: Int?,

    val gender: Gender?,

    @field:Min(value = 1)
    val height: Double?,

    @field:Min(value = 1)
    val weight: Double?,

    val activityLevel: ActivityLevel?
)

