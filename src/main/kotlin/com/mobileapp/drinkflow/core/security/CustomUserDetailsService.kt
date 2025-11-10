package com.mobileapp.drinkflow.core.security

import com.mobileapp.drinkflow.domain.user.entity.User
import com.mobileapp.drinkflow.domain.user.repository.UserRepository
import com.mobileapp.drinkflow.global.exception.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    override fun loadUserByUsername(email: String): UserDetails {
        val user: User = userRepository.findByEmail(email)
            .orElseThrow { ErrorCode.USER_NOT_FOUND.toException() }
        log.debug("loadUserByUsername: {} -> {}", email, user)
        return CustomUserDetails.of(user)
    }

    @Transactional(readOnly = true)
    fun loadUserById(id: Long): UserDetails {
        val user: User = userRepository.findById(id)
            .orElseThrow { ErrorCode.USER_NOT_FOUND.toException() }
        log.debug("loadUserById: {} -> {}", id, user)
        return CustomUserDetails.of(user)
    }
}

