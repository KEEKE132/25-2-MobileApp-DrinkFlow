package com.mobileapp.drinkflow.domain.drinkRecord.repository

import com.mobileapp.drinkflow.domain.drinkRecord.DrinkRecord
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface DrinkRecordRepository : JpaRepository<DrinkRecord, Long> {
    fun findAllByUserId(userId: Long, pageable: Pageable): Page<DrinkRecord>
    fun findAllByUserIdAndDateBetween(
        userId: Long,
        start: LocalDateTime,
        end: LocalDateTime,
        pageable: Pageable
    ): Page<DrinkRecord>
}

