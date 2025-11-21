package com.mobileapp.drinkflow.domain.userProfile.controller

import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileCreateRequest
import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileResponse
import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileUpdateRequest
import com.mobileapp.drinkflow.domain.userProfile.service.UserProfileService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user-profiles")
class UserProfileController(
    private val userProfileService: UserProfileService
) {
    /**
     * 현재 인증된 사용자의 프로필을 생성합니다.
     */
    @PostMapping
    fun createProfile(
        @Valid @RequestBody request: UserProfileCreateRequest
    ): ResponseEntity<UserProfileResponse> {
        val profile = userProfileService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(profile)
    }

    /**
     * 프로필을 업데이트합니다.
     */
    @PatchMapping("/{id}")
    fun updateProfile(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserProfileUpdateRequest
    ): ResponseEntity<UserProfileResponse> {
        val profile = userProfileService.update(id, request)
        return ResponseEntity.ok(profile)
    }

    /**
     * 프로필을 삭제합니다.
     */
    @DeleteMapping("/{id}")
    fun deleteProfile(@PathVariable id: Long): ResponseEntity<Void> {
        userProfileService.delete(id)
        return ResponseEntity.noContent().build()
    }
}

