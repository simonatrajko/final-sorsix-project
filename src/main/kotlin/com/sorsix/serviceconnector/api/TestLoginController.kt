package com.sorsix.serviceconnector.api

import org.springframework.security.core.Authentication
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/secured")
    fun securedEndpoint(authentication: Authentication): String {
        return "Authentication successful. User ID: ${authentication.name}"
    }

}