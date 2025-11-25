package com.mobileapp.drinkflow.domain.drinkRecord.dto

data class UserDailyIntake(
    val userId: Long,
    val username: String,
    val name: String,
    val totalAmount: Int
)

