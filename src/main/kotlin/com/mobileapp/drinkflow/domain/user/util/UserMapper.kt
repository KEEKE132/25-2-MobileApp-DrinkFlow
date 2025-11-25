package com.mobileapp.drinkflow.domain.user.util

import com.mobileapp.drinkflow.domain.user.dto.SignupRequest
import com.mobileapp.drinkflow.domain.user.dto.UserDetailResponse
import com.mobileapp.drinkflow.domain.user.dto.UserResponse
import com.mobileapp.drinkflow.domain.user.entity.User
import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileResponse
import com.mobileapp.drinkflow.domain.userProfile.util.RecommendAmountCalculator.calculateRecommendedAmount

object UserMapper {
    fun fromSignupRequest(request: SignupRequest, encodedPassword: String): User {
        return User(
            username = request.username,
            name = request.name,
            password = encodedPassword
        )
    }

    fun toUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            username = user.username,
            name = user.name
        )
    }

    fun toUserDetailResponse(user: User, todayAmount: Int): UserDetailResponse {
        return UserDetailResponse(
            id = user.id,
            username = user.username,
            name = user.name,
            profile = user.profile?.let {
                UserProfileResponse(
                    id = it.id,
                    age = it.age,
                    gender = it.gender,
                    height = it.height,
                    weight = it.weight,
                    activityLevel = it.activityLevel,
                    userId = user.id,
                    todayAmount = todayAmount,
                    recommendAmount = calculateRecommendedAmount(it)
                )
            }
        )
    }
}