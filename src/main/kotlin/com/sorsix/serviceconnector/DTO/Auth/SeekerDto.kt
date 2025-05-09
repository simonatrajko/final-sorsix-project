package com.sorsix.serviceconnector.DTO

data class SeekerDTO(
    val id: Long,
    val fullName: String,
    val email: String,
    val location: String,
    val preferredContactMethod: String?,
    val notificationPreferences: String?
)
