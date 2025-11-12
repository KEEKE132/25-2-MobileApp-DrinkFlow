package com.mobileapp.drinkflow.domain.user.repository

import com.mobileapp.drinkflow.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsUserByUsername(username: String): Boolean
}

