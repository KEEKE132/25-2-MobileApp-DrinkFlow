package com.mobileapp.drinkflow.domain.user.repository

import com.mobileapp.drinkflow.domain.user.entity.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsUserByUsername(username: String): Boolean

    @EntityGraph(attributePaths = ["profile"])
    @Query("SELECT u FROM User u WHERE u.id = :id")
    fun findByIdWithProfile(@Param("id") id: Long): User?
}

