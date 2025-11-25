package com.mobileapp.drinkflow.domain.userProfile.service

import com.mobileapp.drinkflow.domain.drinkRecord.service.DrinkRecordService
import com.mobileapp.drinkflow.domain.user.repository.UserRepository
import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileCreateRequest
import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileResponse
import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileUpdateRequest
import com.mobileapp.drinkflow.domain.userProfile.repository.UserProfileRepository
import com.mobileapp.drinkflow.domain.userProfile.util.RecommendAmountCalculator.calculateRecommendedAmount
import com.mobileapp.drinkflow.domain.userProfile.util.UserProfileMapper
import com.mobileapp.drinkflow.global.exception.ErrorCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
    private val userRepository: UserRepository,
    private val drinkRecordService: DrinkRecordService
) {

    @Transactional(readOnly = true)
    fun findById(id: Long): UserProfileResponse {
        val userProfile = userProfileRepository.findByIdOrNull(id)
            ?: throw ErrorCode.USER_PROFILE_NOT_FOUND.toException()
        val today =
            drinkRecordService.getDateAmount(userProfile.user.id!!, LocalDateTime.now()) ?: 0
        return UserProfileMapper.toUserProfileResponse(
            userProfile,
            today,
            calculateRecommendedAmount(userProfile)
        )
    }

    @Transactional(readOnly = true)
    fun findByUserId(userId: Long): UserProfileResponse {
        val userProfile = userProfileRepository.findByUserId(userId)
            ?: throw ErrorCode.USER_PROFILE_NOT_FOUND.toException()
        val today = drinkRecordService.getDateAmount(userId, LocalDateTime.now()) ?: 0
        return UserProfileMapper.toUserProfileResponse(
            userProfile,
            today,
            calculateRecommendedAmount(userProfile)
        )
    }

    @Transactional
    fun create(request: UserProfileCreateRequest): UserProfileResponse {
        val user = userRepository.findByIdOrNull(request.userId)
            ?: throw ErrorCode.USER_NOT_FOUND.toException()

        // Check if user already has a profile
        if (user.profile != null) {
            throw ErrorCode.DATA_CONFLICT.toException("사용자는 이미 프로필을 가지고 있습니다.")
        }

        val userProfile = UserProfileMapper.fromCreateRequest(request, user)
        val savedProfile = userProfileRepository.save(userProfile)

        // Link profile to user
        user.profile = savedProfile
        val today = drinkRecordService.getDateAmount(request.userId, LocalDateTime.now()) ?: 0
        return UserProfileMapper.toUserProfileResponse(
            savedProfile,
            today,
            calculateRecommendedAmount(savedProfile)
        )
    }

    @Transactional
    fun update(id: Long, request: UserProfileUpdateRequest): UserProfileResponse {
        val userProfile = userProfileRepository.findByIdOrNull(id)
            ?: throw ErrorCode.USER_PROFILE_NOT_FOUND.toException()

        userProfile.update(request)
        val today =
            drinkRecordService.getDateAmount(userProfile.user.id!!, LocalDateTime.now()) ?: 0
        return UserProfileMapper.toUserProfileResponse(
            userProfile,
            today,
            calculateRecommendedAmount(userProfile)
        )
    }

    @Transactional
    fun delete(id: Long) {
        val userProfile = userProfileRepository.findByIdOrNull(id)
            ?: throw ErrorCode.USER_PROFILE_NOT_FOUND.toException()

        // Unlink from user before deleting
        userProfile.user.profile = null

        userProfileRepository.delete(userProfile)
    }
}

