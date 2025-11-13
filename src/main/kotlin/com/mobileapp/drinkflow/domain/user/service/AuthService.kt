package com.mobileapp.drinkflow.domain.user.service

import com.mobileapp.drinkflow.core.jwt.JwtTokenProvider
import com.mobileapp.drinkflow.core.jwt.RefreshToken
import com.mobileapp.drinkflow.core.jwt.RefreshTokenRepository
import com.mobileapp.drinkflow.core.jwt.TokenPair
import com.mobileapp.drinkflow.core.security.CustomUserDetails
import com.mobileapp.drinkflow.domain.user.dto.LoginDto
import com.mobileapp.drinkflow.domain.user.dto.LoginRequest
import com.mobileapp.drinkflow.domain.user.dto.SignupRequest
import com.mobileapp.drinkflow.domain.user.dto.UserResponse
import com.mobileapp.drinkflow.domain.user.entity.User
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

        refreshTokenRepository.findByUserId(userDetails.user.id)?.let {
            refreshTokenRepository.delete(it)
            refreshTokenRepository.flush()
        }

        saveRefreshToken(tokenPair, userDetails.user)

        return LoginDto(
            UserMapper.toUserResponse(userDetails.user),
            tokenPair
        )
    }

    private fun saveRefreshToken(
        tokenPair: TokenPair,
        user: User
    ) {
        val refreshToken = RefreshToken(
            token = tokenPair.refreshToken,
            user = user
        )
        refreshTokenRepository.save(refreshToken)
    }

    @Transactional
    fun refresh(refreshToken: String): TokenPair {
        try {
            jwtTokenProvider.validate(refreshToken)
        } catch (e: Exception) {
            throw ErrorCode.INVALID_TOKEN.toException()
        }

        refreshTokenRepository.findByToken(refreshToken)?.let {
            val tokenpair = jwtTokenProvider.issueTokenPair(it.user)

            refreshTokenRepository.delete(it)
            refreshTokenRepository.flush()

            saveRefreshToken(tokenpair, it.user)

            return tokenpair
        }
        throw ErrorCode.INVALID_TOKEN.toException()
    }
}