package com.mobileapp.drinkflow.core.jwt

import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class TokenResponseHandler(
    private val environment: Environment
) {

    @PostConstruct
    fun init() {
        val activeProfiles = environment.activeProfiles
        profile = activeProfiles.firstOrNull() ?: "dev"
    }

    companion object {
        private var profile: String = "dev"

        fun setTokens(
            response: HttpServletResponse,
            accessToken: String,
            refreshToken: String
        ) {
            response.setHeader(
                HttpHeaders.AUTHORIZATION,
                "Bearer $accessToken"
            )

            val refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(profile != "dev")
                //.secure(profile != "dev")
                .secure(false)
                .path(if (profile == "dev") "/auth" else "/api/auth")
                .maxAge(3 * 24 * 60 * 60L)
                .sameSite(if (profile == "dev") "Lax" else "None")
                .build()

            response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshCookie.toString()
            )
        }

        fun clearTokens(response: HttpServletResponse) {
            val deleteCookie = ResponseCookie.from("refreshToken")
                .path(if (profile == "dev") "/auth" else "/api/auth")
                .maxAge(0)
                .build()

            response.addHeader(
                HttpHeaders.SET_COOKIE,
                deleteCookie.toString()
            )
        }
    }
}
