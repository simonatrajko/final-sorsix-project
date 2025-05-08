package com.sorsix.serviceconnector.DTO

data class UserInfoDto(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String,
    val role: String
)
