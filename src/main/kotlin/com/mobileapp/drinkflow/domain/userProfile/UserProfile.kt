package com.mobileapp.drinkflow.domain.userProfile

import com.mobileapp.drinkflow.domain.user.entity.User
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
}

enum class Gender {
    MALE, FEMALE, UNKNOWN
}