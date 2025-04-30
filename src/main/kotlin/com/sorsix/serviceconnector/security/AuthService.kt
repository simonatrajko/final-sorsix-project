package com.sorsix.serviceconnector.security

import com.sorsix.serviceconnector.DTO.Auth.AuthResponseDto
import com.sorsix.serviceconnector.DTO.Auth.UserAuthDto
import com.sorsix.serviceconnector.mapper.UserMapper
import com.sorsix.serviceconnector.model.*
import com.sorsix.serviceconnector.repository.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val serviceProviderRepository: ServiceProviderRepository,
    private val serviceSeekerRepository: ServiceSeekerRepository,
    private val hashEncoder: HashEncoder,
) {
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    enum class UserType {
        PROVIDER, SEEKER
    }

    data class RegistrationRequest(
        val username: String,
        val email: String,
        val password: String,
        val fullName: String,
        val location: String,
        val userType: UserType,
        val profileImage: String = "",
        val yearsOfExperience: Int? = null,
        val bio: String? = null,
        val languages: String? = null,
        val preferredContactMethod: String? = null,
        val notificationPreferences: String? = null
    )

    data class LoginRequest(
        val email: String,
        val password: String
    )

    @Transactional
    fun register(request: RegistrationRequest): AuthResponseDto {
        val existingUserByEmail = userRepository.findByEmail(request.email.trim())
        if (existingUserByEmail != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "A user with that email already exists.")
        }

        val user = when (request.userType) {
            UserType.PROVIDER -> ServiceProvider(
                username = request.username,
                hashedPassword = hashEncoder.encode(request.password),
                email = request.email,
                fullName = request.fullName,
                profileImage = request.profileImage,
                location = request.location,
                yearsOfExperience = request.yearsOfExperience,
                bio = request.bio,
                languages = request.languages
            )

            UserType.SEEKER -> ServiceSeeker(
                username = request.username,
                hashedPassword = hashEncoder.encode(request.password),
                email = request.email,
                fullName = request.fullName,
                profileImage = request.profileImage,
                location = request.location,
                preferredContactMethod = request.preferredContactMethod,
                notificationPreferences = request.notificationPreferences
            )
        }

        val savedUser = userRepository.save(user)

        val role = when (savedUser) {
            is ServiceProvider -> "PROVIDER"
            is ServiceSeeker -> "SEEKER"
            else -> "USER"
        }

        val tokens = generateTokenPair(savedUser.username, role)
        val userDto = UserMapper.toDto(savedUser)

        return AuthResponseDto(userDto, tokens)
    }

    fun login(request: LoginRequest): AuthResponseDto {
        val user = userRepository.findByEmail(request.email)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")

        if (!hashEncoder.matches(request.password, user.hashedPassword)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }

        val role = when (user) {
            is ServiceProvider -> "PROVIDER"
            is ServiceSeeker -> "SEEKER"
            else -> "USER"
        }

        val tokens = generateTokenPair(user.username, role)
        val userDto = UserMapper.toDto(user)

        return AuthResponseDto(userDto, tokens)
    }

    fun refreshToken(refreshToken: String): TokenPair {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
        }

        val username = jwtService.getUserIdFromToken(refreshToken)
        val role = jwtService.getRoleFromToken(refreshToken)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token structure")

        return generateTokenPair(username, role)
    }

    private fun generateTokenPair(username: String, role: String): TokenPair {
        val accessToken = jwtService.generateAccessToken(username, role)
        val refreshToken = jwtService.generateRefreshToken(username, role)
        return TokenPair(accessToken, refreshToken)
    }

    @Transactional
    fun changePassword(username: String, currentPassword: String, newPassword: String) {
        val user = userRepository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")

        if (!hashEncoder.matches(currentPassword, user.hashedPassword)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect")
        }

        user.hashedPassword = hashEncoder.encode(newPassword)
        userRepository.save(user)
    }

    fun validateToken(token: String): Boolean {
        return jwtService.validateAccessToken(token)
    }

    fun getUserFromToken(token: String): UserAuthDto {
        val username = jwtService.getUserIdFromToken(token)
        val user = userRepository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        return UserMapper.toDto(user)
    }
}
