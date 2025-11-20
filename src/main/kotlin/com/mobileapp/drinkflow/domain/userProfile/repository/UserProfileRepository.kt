package com.mobileapp.drinkflow.domain.userProfile.repository

import com.mobileapp.drinkflow.domain.userProfile.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, Long> {
    fun findByUserId(userId: Long): UserProfile?
}

