package com.mobileapp.drinkflow.domain.drinkRecord.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class DrinkRecordRequest(
    @field:NotNull
    @field:Min(1)
    val amount: Int,
)

