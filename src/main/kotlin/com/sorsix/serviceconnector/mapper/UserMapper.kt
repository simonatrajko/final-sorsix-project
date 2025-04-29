package com.sorsix.serviceconnector.mapper

import com.sorsix.serviceconnector.DTO.Auth.ServiceProviderAuthDto
import com.sorsix.serviceconnector.DTO.Auth.ServiceSeekerAuthDto
import com.sorsix.serviceconnector.DTO.Auth.UserAuthDto
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.model.ServiceSeeker
import com.sorsix.serviceconnector.model.User

object UserMapper {

    fun toDto(user: User): UserAuthDto {
        return when (user) {
            is ServiceProvider -> toProviderDto(user)
            is ServiceSeeker -> toSeekerDto(user)
            else -> throw IllegalArgumentException("Unknown user type: ${user::class.simpleName}")
        }
    }

    private fun toProviderDto(provider: ServiceProvider): ServiceProviderAuthDto {
        return ServiceProviderAuthDto(
            id = provider.id!!,
            username = provider.username,
            email = provider.email,
            fullName = provider.fullName,
            profileImage = provider.profileImage,
            location = provider.location,
            yearsOfExperience = provider.yearsOfExperience,
            bio = provider.bio,
            languages = provider.languages
        )
    }

    private fun toSeekerDto(seeker: ServiceSeeker): ServiceSeekerAuthDto {
        return ServiceSeekerAuthDto(
            id = seeker.id!!,
            username = seeker.username,
            email = seeker.email,
            fullName = seeker.fullName,
            profileImage = seeker.profileImage,
            location = seeker.location,
            preferredContactMethod = seeker.preferredContactMethod,
            notificationPreferences = seeker.notificationPreferences
        )
    }
}
