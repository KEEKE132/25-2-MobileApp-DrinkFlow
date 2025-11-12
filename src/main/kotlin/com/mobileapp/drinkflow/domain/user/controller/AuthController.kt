package com.mobileapp.drinkflow.domain.user.controller

import com.mobileapp.drinkflow.domain.user.dto.SignupRequest
import com.mobileapp.drinkflow.domain.user.dto.UserResponse
import com.mobileapp.drinkflow.domain.user.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    public fun signup(@RequestBody @Valid signupRequest: SignupRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupRequest))

    }
}