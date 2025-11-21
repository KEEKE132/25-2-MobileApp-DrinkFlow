package com.mobileapp.drinkflow.domain.user.controller

import com.mobileapp.drinkflow.core.jwt.JwtPrincipal
import com.mobileapp.drinkflow.domain.user.dto.UserDetailResponse
import com.mobileapp.drinkflow.domain.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal jwtPrincipal: JwtPrincipal): ResponseEntity<UserDetailResponse> {
        return ResponseEntity.ok(userService.findById(jwtPrincipal.id))
    }
}