package com.mobileapp.drinkflow.domain.userProfile.util

import com.mobileapp.drinkflow.domain.userProfile.ActivityLevel
import com.mobileapp.drinkflow.domain.userProfile.Gender
import com.mobileapp.drinkflow.domain.userProfile.UserProfile

object RecommendAmountCalculator {

    fun calculateRecommendedAmount(profile: UserProfile): Int {
        var result = profile.weight * 35.0

        if (profile.gender == Gender.MALE) result *= 1.05
        if (profile.age >= 70) result *= 0.90  // 0.95 * 0.95
        else if (profile.age >= 55) result *= 0.95

        when (profile.activityLevel) {
            ActivityLevel.LOW -> result *= 0.85
            ActivityLevel.HIGH -> result *= 1.2
            else -> {}
        }

        return result.toInt()
    }
}