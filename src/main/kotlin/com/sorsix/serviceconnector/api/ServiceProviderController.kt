package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.DTO.ProviderDTO
import com.sorsix.serviceconnector.service.ServiceProviderService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/providers")
class ProviderController(
    private val providerService: ServiceProviderService
) {
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    fun searchProviders(@RequestParam name: String): ResponseEntity<List<ProviderDTO>> {
        val results = providerService.searchProvidersByName(name)
        val dtos = results.map {
            ProviderDTO(
                id = it.id!!,
                fullName = it.fullName,
                email = it.email,
                location = it.location,
                yearsOfExperience = it.yearsOfExperience,
                bio = it.bio,
                languages = it.languages
            )
        }
        return ResponseEntity.ok(dtos)
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    fun getProviderById(@PathVariable id: Long): ResponseEntity<ProviderDTO> {
        val provider = providerService.getProvider(id)
        val dto = ProviderDTO(
            id = provider.id!!,
            fullName = provider.fullName,
            email = provider.email,
            location = provider.location,
            yearsOfExperience = provider.yearsOfExperience,
            bio = provider.bio,
            languages = provider.languages
        )
        return ResponseEntity.ok(dto)
    }
}
