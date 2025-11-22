package com.mobileapp.drinkflow.domain.drinkRecord.util

import com.mobileapp.drinkflow.domain.drinkRecord.DrinkRecord
import com.mobileapp.drinkflow.domain.drinkRecord.dto.DrinkRecordPageResponse
import com.mobileapp.drinkflow.domain.drinkRecord.dto.DrinkRecordRequest
import com.mobileapp.drinkflow.domain.drinkRecord.dto.DrinkRecordResponse
import com.mobileapp.drinkflow.domain.user.entity.User
import org.springframework.data.domain.Page
import java.time.LocalDateTime

object DrinkRecordMapper {
    fun fromRequest(request: DrinkRecordRequest, user: User): DrinkRecord {
        return DrinkRecord(
            amount = request.amount,
            date = LocalDateTime.now(),
            user = user
        )
    }

    fun toResponse(drinkRecord: DrinkRecord): DrinkRecordResponse {
        return DrinkRecordResponse(
            id = drinkRecord.id,
            amount = drinkRecord.amount,
            date = drinkRecord.date,
            userId = drinkRecord.user.id
        )
    }

    fun toPageResponse(page: Page<DrinkRecord>): DrinkRecordPageResponse {
        return DrinkRecordPageResponse(
            records = page.content.map { toResponse(it) },
            page = page.number,
            size = page.size,
            totalPages = page.totalPages,
            totalElements = page.totalElements
        )
    }
}

