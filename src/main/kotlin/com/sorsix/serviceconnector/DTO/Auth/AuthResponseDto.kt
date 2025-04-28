package com.sorsix.serviceconnector.DTO.Auth

import com.sorsix.serviceconnector.security.AuthService

data class AuthResponseDto(
    val user: UserAuthDto,
    val tokens: AuthService.TokenPair
)