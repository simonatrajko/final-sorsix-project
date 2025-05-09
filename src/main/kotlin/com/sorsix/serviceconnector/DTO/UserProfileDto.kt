package com.sorsix.serviceconnector.DTO

data class UserProfileDTO(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String,
    val role: String,
    val location: String,

    // Provider-only fields
    val yearsOfExperience: Int? = null,
    val bio: String? = null,
    val languages: String? = null,

    // Seeker-only fields
    val preferredContactMethod: String? = null,
    val notificationPreferences: String? = null
)
