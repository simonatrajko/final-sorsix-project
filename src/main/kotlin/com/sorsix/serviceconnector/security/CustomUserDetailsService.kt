package com.sorsix.serviceconnector.security

import com.sorsix.serviceconnector.repository.ServiceProviderRepository
import com.sorsix.serviceconnector.repository.ServiceSeekerRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val providerRepository: ServiceProviderRepository,
    private val seekerRepository: ServiceSeekerRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = providerRepository.findByUsername(username)
            ?: seekerRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found")

        return AppUserDetails(user)
    }

}
