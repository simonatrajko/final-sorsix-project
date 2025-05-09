package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.DTO.CreateServiceRequest
import com.sorsix.serviceconnector.DTO.ServiceDTO
import com.sorsix.serviceconnector.DTO.UpdatePriceRequest
import com.sorsix.serviceconnector.mapper.toDto
import com.sorsix.serviceconnector.mapper.toEntity
import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.repository.ServiceCategoryRepository
import com.sorsix.serviceconnector.repository.ServiceProviderRepository
import com.sorsix.serviceconnector.service.impl.ServicesServiceImpl
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@Validated
@RestController
@RequestMapping("/api/services")
class ServiceController(
    private val servicesService: ServicesServiceImpl,
    private val serviceProviderRepository: ServiceProviderRepository,
    private val serviceCategoryRepository: ServiceCategoryRepository,
) {
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    fun getAllAvailableServices(pageable: Pageable): Page<ServiceDTO> =
        servicesService.getAvailableServicesAsDto(pageable)

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getServiceById(@PathVariable id: Long): ResponseEntity<ServiceDTO> =
        servicesService.getServiceById(id)
            ?.let { ResponseEntity.ok(it.toDto()) }
            ?: ResponseEntity.notFound().build()

    @PostMapping
    @PreAuthorize("hasRole('PROVIDER')")
    fun createService(
        @Valid @RequestBody request: CreateServiceRequest,
        @AuthenticationPrincipal user: UserDetails?
    ): ResponseEntity<ServiceDTO> {

        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated")
        }

        val provider = serviceProviderRepository.findByUsername(user.username)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Provider not found")

        val category = serviceCategoryRepository.findById(request.categoryId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found") }

        val service = request.toEntity(category, provider)
        val created = servicesService.createService(service)

        return ResponseEntity.status(HttpStatus.CREATED).body(created.toDto())
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER')")
    fun deleteService(@PathVariable id: Long): ResponseEntity<Void> {
        servicesService.deleteService(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/category/{categoryId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("isAuthenticated()")
    fun getServicesByCategory(@PathVariable categoryId: Long): ResponseEntity<List<ServiceDTO>> {
        val category = ServiceCategory(id = categoryId, name = "")
        val services = servicesService.getServicesByCategory(category)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(services.map { it.toDto() })
    }

    @GetMapping("/category/name/{categoryName}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @PreAuthorize("isAuthenticated()")
    fun getServicesByCategoryName(@PathVariable categoryName: String): ResponseEntity<List<ServiceDTO>> {
        val services = servicesService.getServicesByCategoryName(categoryName)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(services.map { it.toDto() })

    }

    @GetMapping("/myServicesProvider")
    @PreAuthorize("isAuthenticated()")
    fun getMyServices(@AuthenticationPrincipal user: UserDetails?): ResponseEntity<List<ServiceDTO>> {
        println(user)
        if (user == null)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated")

        val provider = serviceProviderRepository.findByUsername(user.username)
            ?: return ResponseEntity.notFound().build()

        val services = servicesService.getServicesByProvider(provider.id!!)
            .map { it.toDto() }

        return ResponseEntity.ok(services)
    }

    @PatchMapping("/{id}/price")
    @PreAuthorize("hasRole('PROVIDER')")
    fun updateServicePrice(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdatePriceRequest,
        @AuthenticationPrincipal user: UserDetails?
    ): ResponseEntity<ServiceDTO> {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated")
        }

        val provider = serviceProviderRepository.findByUsername(user.username)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Provider not found")

        val updated = servicesService.updatePrice(id, request.price!!, provider.id!!)
        return ResponseEntity.ok(updated.toDto())
    }

}