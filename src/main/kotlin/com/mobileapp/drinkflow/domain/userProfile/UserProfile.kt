package com.mobileapp.drinkflow.domain.userProfile

import com.mobileapp.drinkflow.domain.user.entity.User
import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileUpdateRequest
import jakarta.persistence.*

@Entity
class UserProfile(

    var age: Int,

    var weight: Double,

    @Enumerated(EnumType.STRING)
    var gender: Gender,

    @Enumerated(EnumType.STRING)
    var activityLevel: ActivityLevel,

    var height: Double,

    @OneToOne(mappedBy = "profile")
    val user: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    fun update(request: UserProfileUpdateRequest) {
        request.age?.let { this.age = it }
        request.gender?.let { this.gender = it }
        request.height?.let { this.height = it }
        request.weight?.let { this.weight = it }
        request.activityLevel?.let { this.activityLevel = it }
    }
}

enum class Gender {
    MALE, FEMALE, UNKNOWN
}

enum class ActivityLevel {
    LOW, MEDIUM, HIGH
}