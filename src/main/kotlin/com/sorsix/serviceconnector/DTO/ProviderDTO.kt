package com.sorsix.serviceconnector.DTO

data class ProviderDTO(
    val id: Long,
    val fullName: String,
    val email: String,
    val location: String,
    val yearsOfExperience: Int?,
    val bio: String?,
    val languages: String?
)
