package com.mobileapp.drinkflow.domain.drinkRecord.dto

import java.time.LocalDateTime

data class DrinkRecordResponse(
    val id: Long?,
    val amount: Int,
    val date: LocalDateTime,
    val userId: Long?
)

