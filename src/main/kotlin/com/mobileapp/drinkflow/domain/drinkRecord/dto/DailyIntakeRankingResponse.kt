package com.mobileapp.drinkflow.domain.drinkRecord.dto

data class DailyIntakeRankingResponse(
    val userId: Long,
    val username: String,
    val name: String,
    val totalAmount: Int,
    val rank: Int
)

