package com.mobileapp.drinkflow.core.security

import com.mobileapp.drinkflow.domain.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class CustomUserDetails(
    private val username: String,
    private val password: String,
    val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("USER"))
    }

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    companion object {
        fun of(user: User): CustomUserDetails {
            return CustomUserDetails(
                username = user.username,
                password = user.password,
                user = user
            )
        }
    }
}

