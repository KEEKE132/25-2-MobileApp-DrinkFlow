package com.mobileapp.drinkflow.domain.user.entity

import com.mobileapp.drinkflow.domain.drinkRecord.DrinkRecord
import com.mobileapp.drinkflow.domain.userProfile.UserProfile
import jakarta.persistence.*

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

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var drinkRecords: MutableList<DrinkRecord> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var friendships: MutableList<Friendship> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var profile: UserProfile? = null

}