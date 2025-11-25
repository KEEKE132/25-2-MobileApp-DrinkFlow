package com.mobileapp.drinkflow.domain.drinkRecord.repository

import com.mobileapp.drinkflow.domain.drinkRecord.DrinkRecord
import com.mobileapp.drinkflow.domain.drinkRecord.dto.UserDailyIntake
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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

    @Query(
        """
    SELECT COALESCE(SUM(d.amount), 0)
    FROM DrinkRecord d
    WHERE d.user.id = :userId
    AND d.date >= :startDate
    AND d.date < :endDate
    """
    )
    fun sumAmountByUserIdAndDateBetween(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Int

    @Query(
        """
    SELECT new com.mobileapp.drinkflow.domain.drinkRecord.dto.UserDailyIntake(
        u.id,
        u.username,
        u.name,
        CAST(COALESCE(SUM(CASE WHEN d.date >= :startDate AND d.date < :endDate THEN d.amount ELSE 0 END), 0) AS int)
    )
    FROM User u
    LEFT JOIN u.drinkRecords d
    GROUP BY u.id, u.username, u.name
    ORDER BY COALESCE(SUM(CASE WHEN d.date >= :startDate AND d.date < :endDate THEN d.amount ELSE 0 END), 0) DESC, u.id ASC
    """
    )
    fun findAllUsersDailyIntakeFirstPage(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<UserDailyIntake>
}

