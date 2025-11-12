package com.mobileapp.drinkflow.domain.user.util

import com.mobileapp.drinkflow.domain.user.dto.SignupRequest
import com.mobileapp.drinkflow.domain.user.dto.UserResponse
import com.mobileapp.drinkflow.domain.user.entity.User

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
            username = user.username,
            name = user.name
        )
    }
}