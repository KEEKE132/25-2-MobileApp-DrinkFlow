package com.mobileapp.drinkflow.core.jwt.filter

import com.mobileapp.drinkflow.core.jwt.JwtPrincipal
import com.mobileapp.drinkflow.core.jwt.JwtTokenProvider
import com.mobileapp.drinkflow.core.jwt.TokenBody
import com.mobileapp.drinkflow.core.security.CustomEntryPoint
import com.mobileapp.drinkflow.core.security.CustomUserDetailsService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.List

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val customUserDetailsService: CustomUserDetailsService,
    private val customEntryPoint: CustomEntryPoint
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        // OPTIONS 요청은 JWT 검증 없이 통과
        if ("OPTIONS".equals(request.method, ignoreCase = true)) {
            filterChain.doFilter(request, response)
            return
        }
        val jwt = getJwtFromRequest(request)

            jwt?.let {
                try{
                    try{
                        jwtTokenProvider.validate(it)
                        setAuthentication(it)
                    } catch (e: ExpiredJwtException){
                        SecurityContextHolder.clearContext()
                        customEntryPoint.commenceExpiredToken(response)
                    } catch (e: JwtException) {
                        throw BadCredentialsException("Invalid token")
                    } catch (e:IllegalArgumentException){
                        throw BadCredentialsException("Invalid token")
                    }
                } catch (e: AuthenticationException) {
                    SecurityContextHolder.clearContext()
                    customEntryPoint.commence(request, response, e)
                    return
                }

            }

        filterChain.doFilter(request, response)
    }

    private fun setAuthentication(accessToken: String) {
        val tokenBody: TokenBody = jwtTokenProvider.parseJwt(accessToken)
        val jwtPrincipal: JwtPrincipal = JwtPrincipal.of(tokenBody)
        val authentication = UsernamePasswordAuthenticationToken(
            jwtPrincipal,
            null,
            List.of<SimpleGrantedAuthority?>(SimpleGrantedAuthority("USER"))
        )

        SecurityContextHolder.getContext().setAuthentication(authentication)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
}

