package com.mobileapp.drinkflow.domain.user.controller

import com.mobileapp.drinkflow.core.jwt.TokenPair
import com.mobileapp.drinkflow.core.jwt.TokenResponseHandler
import com.mobileapp.drinkflow.domain.user.dto.LoginRequest
import com.mobileapp.drinkflow.domain.user.dto.SignupRequest
import com.mobileapp.drinkflow.domain.user.dto.UserResponse
import com.mobileapp.drinkflow.domain.user.service.AuthService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    fun signup(@RequestBody @Valid signupRequest: SignupRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupRequest))

    }

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid loginRequest: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<UserResponse> {
        val data = authService.login(loginRequest)

        TokenResponseHandler.setTokens(response, data.tokens.accessToken, data.tokens.refreshToken)

        return ResponseEntity.ok(data.user)

    }

    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue refreshToken: String,
        response: HttpServletResponse
    ): ResponseEntity<Void> {
        val newTokens: TokenPair = authService.refresh(refreshToken)

        // 새 토큰을 쿠키에 설정
        TokenResponseHandler.setTokens(
            response, newTokens.accessToken, newTokens.refreshToken
        )
        return ResponseEntity.ok().build()
    }
}