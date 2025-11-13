package com.mobileapp.drinkflow.domain.user.service

import com.mobileapp.drinkflow.domain.user.dto.UserResponse
import com.mobileapp.drinkflow.domain.user.repository.UserRepository
import com.mobileapp.drinkflow.domain.user.util.UserMapper
import com.mobileapp.drinkflow.global.exception.ErrorCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service

class UserService(
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun findById(id: Long): UserResponse {
        val user = userRepository.findByIdOrNull(id) ?: throw ErrorCode.USER_NOT_FOUND.toException()
        return UserMapper.toUserResponse(user)
    }
}