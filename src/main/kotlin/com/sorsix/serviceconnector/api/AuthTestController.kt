package com.sorsix.serviceconnector.api

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test-auth")
class AuthTestController {

    @GetMapping("/public")
    fun publicEndpoint(): String {
        return "This endpoint is public"
    }

    @GetMapping("/authenticated")
    fun authenticatedEndpoint(): String {
        return "This endpoint requires authentication"
    }

    @GetMapping("/provider")
    @PreAuthorize("hasRole('PROVIDER')")
    fun providerEndpoint(): String {
        return "This endpoint is for providers only"
    }

    @GetMapping("/seeker")
    @PreAuthorize("hasRole('SEEKER')")
    fun seekerEndpoint(): String {
        return "This endpoint is for seekers only"
    }

}