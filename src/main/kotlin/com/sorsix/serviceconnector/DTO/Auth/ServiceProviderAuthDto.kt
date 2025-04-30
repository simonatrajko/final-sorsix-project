package com.sorsix.serviceconnector.DTO.Auth

data class ServiceProviderAuthDto(
    override val id: Long,
    override val username: String,
    override val email: String,
    override val fullName: String,
    override val profileImage: String?,
    override val location: String?,
    override val userType: String = "PROVIDER",
    val yearsOfExperience: Int?,
    val bio: String?,
    val languages: String?
) : UserAuthDto

