package com.sorsix.serviceconnector.DTO.Auth

data class ServiceSeekerAuthDto(
    override val id: Long,
    override val username: String,
    override val email: String,
    override val fullName: String,
    override val profileImage: String?,
    override val location: String?,
    override val userType: String = "SEEKER",
    val preferredContactMethod: String?,
    val notificationPreferences: String?
) : UserAuthDto