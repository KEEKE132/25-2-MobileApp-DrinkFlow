package com.mobileapp.drinkflow.domain.user.service

import com.mobileapp.drinkflow.core.jwt.JwtTokenProvider
import com.mobileapp.drinkflow.core.jwt.RefreshToken
import com.mobileapp.drinkflow.core.jwt.RefreshTokenRepository
import com.mobileapp.drinkflow.core.security.CustomUserDetails
import com.mobileapp.drinkflow.domain.user.dto.LoginDto
import com.mobileapp.drinkflow.domain.user.dto.LoginRequest
import com.mobileapp.drinkflow.domain.user.dto.SignupRequest
import com.mobileapp.drinkflow.domain.user.dto.UserResponse
import com.mobileapp.drinkflow.domain.user.repository.UserRepository
import com.mobileapp.drinkflow.domain.user.util.UserMapper
import com.mobileapp.drinkflow.global.exception.ErrorCode
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val refreshTokenRepository: RefreshTokenRepository
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

    @Transactional
    fun login(request: LoginRequest): LoginDto {
        val authenticationToken =
            UsernamePasswordAuthenticationToken(request.username, request.password)
        val auth = authenticationManager.authenticate(authenticationToken)
        val userDetails = auth.principal as CustomUserDetails

        val tokenPair = jwtTokenProvider.issueTokenPair(userDetails.user)

        val userRefreshToken = refreshTokenRepository.findByUserId(userDetails.user.id)?.let {
            refreshTokenRepository.delete(it)
            refreshTokenRepository.flush()
        }

        val refreshToken = RefreshToken(
            token = tokenPair.refreshToken,
            user = userDetails.user
        )
        refreshTokenRepository.save(refreshToken)

        return LoginDto(
            UserMapper.toUserResponse(userDetails.user),
            tokenPair
        )
    }
}