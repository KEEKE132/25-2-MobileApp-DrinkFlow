package com.mobileapp.drinkflow.domain.user.dto

import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileResponse

data class UserDetailResponse(
    val id: Long?,
    val username: String?,
    val name: String?,
    val profile: UserProfileResponse?
)
