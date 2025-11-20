package com.mobileapp.drinkflow.domain.userProfile

import com.mobileapp.drinkflow.domain.user.entity.User
import com.mobileapp.drinkflow.domain.userProfile.dto.UserProfileUpdateRequest
import jakarta.persistence.*

@Entity
class UserProfile(

    var age: Int,
    var gender: Gender,
    var height: Int,

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
    }
}

enum class Gender {
    MALE, FEMALE, UNKNOWN
}