package com.mobileapp.drinkflow.domain.userProfile.dto

import com.mobileapp.drinkflow.domain.userProfile.ActivityLevel
import com.mobileapp.drinkflow.domain.userProfile.Gender

data class UserProfileResponse(
    val id: Long?,
    val age: Int?,
    val gender: Gender?,
    val height: Double?,
    val weight: Double?,
    val activityLevel: ActivityLevel,
    val userId: Long?
)

