package com.mobileapp.drinkflow.domain.userProfile.util

import com.mobileapp.drinkflow.domain.user.entity.User
import com.mobileapp.drinkflow.domain.userProfile.UserProfile
import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileCreateRequest
import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileResponse

object UserProfileMapper {
    fun fromCreateRequest(request: UserProfileCreateRequest, user: User): UserProfile {
        return UserProfile(
            age = request.age,
            gender = request.gender,
            height = request.height,
            user = user
        )
    }

    fun toUserProfileResponse(userProfile: UserProfile): UserProfileResponse {
        return UserProfileResponse(
            id = userProfile.id,
            age = userProfile.age,
            gender = userProfile.gender,
            height = userProfile.height,
            userId = userProfile.user.id
        )
    }
}

