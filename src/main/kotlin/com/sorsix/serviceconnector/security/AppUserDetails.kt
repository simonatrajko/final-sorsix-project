package com.sorsix.serviceconnector.security

import com.sorsix.serviceconnector.model.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AppUserDetails(
    private val user: User
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val role = when (user) {
            is ServiceProvider -> "PROVIDER"
            is ServiceSeeker -> "SEEKER"
            else -> "USER"
        }
        return mutableListOf(SimpleGrantedAuthority("ROLE_$role"))
    }

    override fun getPassword(): String = user.hashedPassword
    override fun getUsername(): String = user.username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

    fun getUser(): User = user
}
