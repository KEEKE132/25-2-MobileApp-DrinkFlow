package com.mobileapp.drinkflow.domain.user.entity

import jakarta.persistence.*
import java.time.LocalDateTime

enum class UserType {
    MEMBER, MANAGER, ADMIN
}

@Entity
@Table(name = "users")
class User(
    @Column(nullable = false, length = 50)
    var name: String,

    @Column(nullable = false, unique = true, length = 100)
    var username: String,

    @Column(nullable = false)
    var password: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set
}