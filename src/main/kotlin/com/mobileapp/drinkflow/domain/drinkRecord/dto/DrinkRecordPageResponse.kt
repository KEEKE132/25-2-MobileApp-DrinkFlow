package com.mobileapp.drinkflow.domain.drinkRecord.dto

data class DrinkRecordPageResponse(
    val records: List<DrinkRecordResponse>,
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Long
)

