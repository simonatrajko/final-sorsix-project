package com.sorsix.serviceconnector.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.removePrefix("Bearer ")

        if (!jwtService.validateAccessToken(token)) {
            filterChain.doFilter(request, response)
            return
        }

        val username = jwtService.getUserIdFromToken(token) // this should return username!
        val userDetails = userDetailsService.loadUserByUsername(username)
        val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

        SecurityContextHolder.getContext().authentication = auth
        filterChain.doFilter(request, response)
    }
}

class CustomAuthentication(
    private val userId: String,
    private val role: String?
) : org.springframework.security.core.Authentication {
    override fun getName(): String = userId
    override fun getAuthorities(): Collection<org.springframework.security.core.GrantedAuthority> =
        listOfNotNull(role?.let { org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_$it") })
    override fun getCredentials(): Any? = null
    override fun getDetails(): Any? = null
    override fun getPrincipal(): Any = userId
    override fun isAuthenticated(): Boolean = true
    override fun setAuthenticated(isAuthenticated: Boolean) {
        throw UnsupportedOperationException("Cannot change authentication status")
    }
}
