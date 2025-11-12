package com.mobileapp.drinkflow.core.jwt

import com.mobileapp.drinkflow.domain.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
class RefreshToken(
    @Column(updatable = false, nullable = false)
    val token: String,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        nullable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
        unique = true
    )
    val user: User
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null


    @CreatedDate
    @Column(updatable = false, nullable = false)
    var issuedAt: LocalDateTime? = null


}