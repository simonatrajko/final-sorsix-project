package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.DTO.Auth.AuthResponseDto
import com.sorsix.serviceconnector.DTO.Auth.UserAuthDto
import com.sorsix.serviceconnector.security.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody request: AuthService.RegistrationRequest): ResponseEntity<AuthResponseDto> {
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthService.LoginRequest): ResponseEntity<AuthResponseDto> {
        return ResponseEntity.ok(authService.login(request))
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: Map<String, String>): ResponseEntity<AuthService.TokenPair> {
        val refreshToken = request["refreshToken"]
            ?: return ResponseEntity.badRequest().build()

        return ResponseEntity.ok(authService.refreshToken(refreshToken))
    }

    @GetMapping("/user-info")
    fun getUserInfo(@RequestHeader("Authorization") authHeader: String): ResponseEntity<UserAuthDto> {
        val token = authHeader.removePrefix("Bearer ")
        return ResponseEntity.ok(authService.getUserFromToken(token))
    }
}