package com.mobileapp.drinkflow.domain.drinkRecord.service

import com.mobileapp.drinkflow.domain.drinkRecord.dto.*
import com.mobileapp.drinkflow.domain.drinkRecord.repository.DrinkRecordRepository
import com.mobileapp.drinkflow.domain.drinkRecord.util.DrinkRecordMapper
import com.mobileapp.drinkflow.domain.user.repository.UserRepository
import com.mobileapp.drinkflow.global.exception.ErrorCode
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class DrinkRecordService(
    private val drinkRecordRepository: DrinkRecordRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun create(userId: Long, request: DrinkRecordRequest): DrinkRecordResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { ErrorCode.USER_NOT_FOUND.toException() }

        val record = DrinkRecordMapper.fromRequest(request, user)
        val saved = drinkRecordRepository.save(record)

        return DrinkRecordMapper.toResponse(saved)
    }

    @Transactional(readOnly = true)
    fun get(recordId: Long): DrinkRecordResponse {
        val record = drinkRecordRepository.findByIdOrNull(recordId)
            ?: throw ErrorCode.DRINK_RECORD_NOT_FOUND.toException()
        return DrinkRecordMapper.toResponse(record)
    }

    @Transactional(readOnly = true)
    fun list(
        userId: Long,
        page: Int,
        size: Int,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): DrinkRecordPageResponse {
        val pageable = PageRequest.of(page, size)

        val recordsPage = when {
            startDate != null && endDate != null ->
                drinkRecordRepository
                    .findAllByUserIdAndDateBetween(userId, startDate, endDate, pageable)

            else ->
                drinkRecordRepository
                    .findAllByUserId(userId, pageable)
        }

        return DrinkRecordMapper.toPageResponse(recordsPage)
    }

    @Transactional
    fun update(userId: Long, recordId: Long, request: DrinkRecordRequest): DrinkRecordResponse {
        val record = drinkRecordRepository.findByIdOrNull(recordId)
            ?: throw ErrorCode.DRINK_RECORD_NOT_FOUND.toException()

        if (record.user.id != userId) {
            throw ErrorCode.FORBIDDEN.toException()
        }

        record.update(request.amount)
        return DrinkRecordMapper.toResponse(record)
    }

    @Transactional
    fun delete(userId: Long, recordId: Long) {
        val record = drinkRecordRepository.findByIdOrNull(recordId)
            ?: throw ErrorCode.DRINK_RECORD_NOT_FOUND.toException()
        if (record.user.id != userId) {
            throw ErrorCode.FORBIDDEN.toException()
        }
        drinkRecordRepository.delete(record)
    }

    @Transactional(readOnly = true)
    fun getDateAmount(userId: Long, date: LocalDateTime): Int? {
        val startDate = date.toLocalDate().atStartOfDay()
        val endDate = date.toLocalDate().plusDays(1).atStartOfDay()
        val amount =
            drinkRecordRepository.sumAmountByUserIdAndDateBetween(userId, startDate, endDate)

        return amount
    }

    @Transactional(readOnly = true)
    fun getTodayRanking(): DailyIntakeRankingPageResponse {
        val today = LocalDateTime.now().toLocalDate().atStartOfDay()
        val tomorrow = today.plusDays(1)

        // 전체 사용자의 금일 섭취량 조회
        val intakeList = drinkRecordRepository.findAllUsersDailyIntakeFirstPage(
            today,
            tomorrow
        )

        // 순위 계산 (동점자 처리)
        val rankedList = mutableListOf<DailyIntakeRankingResponse>()
        var currentRank = 1
        var previousAmount: Int? = null

        intakeList.forEachIndexed { index, intake ->
            if (previousAmount != null && previousAmount != intake.totalAmount) {
                currentRank = index + 1
            }

            rankedList.add(
                DailyIntakeRankingResponse(
                    userId = intake.userId,
                    username = intake.username,
                    name = intake.name,
                    totalAmount = intake.totalAmount,
                    rank = currentRank
                )
            )

            previousAmount = intake.totalAmount
        }

        return DailyIntakeRankingPageResponse(
            rankings = rankedList
        )
    }
}
