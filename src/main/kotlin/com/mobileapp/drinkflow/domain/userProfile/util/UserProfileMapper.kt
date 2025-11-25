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
            weight = request.weight,
            activityLevel = request.activityLevel,
            user = user
        )
    }

    fun toUserProfileResponse(userProfile: UserProfile, recommendAmount: Int): UserProfileResponse {
        return UserProfileResponse(
            id = userProfile.id,
            age = userProfile.age,
            gender = userProfile.gender,
            height = userProfile.height,
            weight = userProfile.weight,
            activityLevel = userProfile.activityLevel,
            userId = userProfile.user.id,
            recommendAmount = recommendAmount
        )
    }
}

