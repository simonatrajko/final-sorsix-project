package com.sorsix.serviceconnector.DTO.Auth

interface UserAuthDto {
    val id: Long
    val username: String
    val email: String
    val fullName: String
    val profileImage: String?
    val location: String?
    val userType: String
}
