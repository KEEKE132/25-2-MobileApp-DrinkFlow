package com.mobileapp.drinkflow.domain.user.service

import com.mobileapp.drinkflow.core.jwt.JwtTokenProvider
import com.mobileapp.drinkflow.domain.user.dto.SignupRequest
import com.mobileapp.drinkflow.domain.user.dto.UserResponse
import com.mobileapp.drinkflow.domain.user.repository.UserRepository
import com.mobileapp.drinkflow.domain.user.util.UserMapper
import com.mobileapp.drinkflow.global.exception.ErrorCode
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    val passwordEncoder: PasswordEncoder,
    val jwtTokenProvider: JwtTokenProvider,
    val userRepository: UserRepository
) {
    @Transactional
    fun signup(signupRequest: SignupRequest): UserResponse? {
        if (userRepository.existsUserByUsername(signupRequest.username)) {
            throw ErrorCode.DATA_CONFLICT.toException()
        }
        val encodedPassword = passwordEncoder.encode(signupRequest.password)
        val user = userRepository.save(UserMapper.fromSignupRequest(signupRequest, encodedPassword))
        return UserMapper.toUserResponse(user)
    }
}