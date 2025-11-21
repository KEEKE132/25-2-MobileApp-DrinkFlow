package com.mobileapp.drinkflow.domain.user.service

import com.mobileapp.drinkflow.domain.user.dto.UserDetailResponse
import com.mobileapp.drinkflow.domain.user.repository.UserRepository
import com.mobileapp.drinkflow.domain.user.util.UserMapper
import com.mobileapp.drinkflow.global.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service

class UserService(
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun findById(id: Long): UserDetailResponse {
        val user =
            userRepository.findByIdWithProfile(id) ?: throw ErrorCode.USER_NOT_FOUND.toException()
        return UserMapper.toUserDetailResponse(user)
    }
}