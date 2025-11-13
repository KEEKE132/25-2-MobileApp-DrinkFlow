package com.mobileapp.drinkflow.core.jwt

import java.security.Principal


class JwtPrincipal(
    val id: Long,
    val username: String
) : Principal {

    override fun getName(): String {
        return username;
    }

    companion object {
        fun of(tokenBody: TokenBody): JwtPrincipal {
            return JwtPrincipal(
                id = tokenBody.id,
                username = tokenBody.username
            )
        }
    }
}
