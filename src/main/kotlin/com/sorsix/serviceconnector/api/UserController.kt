package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer = object : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // Angular порт
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
        }
    }
}



@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRepository: UserRepository
) {

    @GetMapping("/me/profile-image-url")
    fun getProfileImageUrl(): ResponseEntity<String> {
        val auth = SecurityContextHolder.getContext().authentication
        val username = auth?.name ?: return ResponseEntity.status(401).build()

        val user = userRepository.findByUsername(username)
            ?: return ResponseEntity.notFound().build()

        val imageUrl = user.profileImage?.takeIf { it.isNotBlank() }
            ?: "/uploads/defaults/default-avatar.jpg"

        return ResponseEntity.ok(imageUrl)
    }
}

