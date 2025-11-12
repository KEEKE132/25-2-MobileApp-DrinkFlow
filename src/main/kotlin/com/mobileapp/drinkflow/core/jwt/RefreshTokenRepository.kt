package com.mobileapp.drinkflow.core.jwt

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken?, Long?> {
    fun findByUserId(userId: Long?): RefreshToken?

    fun findByToken(token: String?): RefreshToken?

    fun deleteByToken(token: String?)
}