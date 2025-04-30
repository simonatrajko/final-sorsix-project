package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.DTO.CreateServiceRequest
import com.sorsix.serviceconnector.DTO.ServiceDTO
import com.sorsix.serviceconnector.mapper.toDto
import com.sorsix.serviceconnector.mapper.toEntity
import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.repository.ServiceCategoryRepository
import com.sorsix.serviceconnector.repository.ServiceProviderRepository
import com.sorsix.serviceconnector.service.impl.ScheduleSlotServiceImpl
import com.sorsix.serviceconnector.service.impl.ServicesServiceImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/services")
class ServiceController(
    private val servicesService: ServicesServiceImpl,
    private val scheduleSlotService: ScheduleSlotServiceImpl,
    private val serviceProviderRepository: ServiceProviderRepository,
    private val serviceCategoryRepository: ServiceCategoryRepository
) {
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    fun getAllAvailableServices(pageable: Pageable): Page<ServiceDTO> =
        servicesService.getAvailableServicesAsDto(pageable)

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getServiceById(@PathVariable id: Long): ResponseEntity<Services> =
        servicesService.getServiceById(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping
    @PreAuthorize("hasRole('PROVIDER')")
    fun createService(
        @RequestBody request: CreateServiceRequest,
        @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<ServiceDTO> {
        val provider = serviceProviderRepository.findByUsername(user.username)
            ?: throw RuntimeException("Provider not found")

        val category = serviceCategoryRepository.findById(request.categoryId)
            .orElseThrow { RuntimeException("Category not found") }

        val service = request.toEntity(category, provider)

        val created = servicesService.createService(service)
        return ResponseEntity.status(HttpStatus.CREATED).body(created.toDto())
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PROVIDER')")
    fun deleteService(@PathVariable id: Long): ResponseEntity<Void> {
        servicesService.deleteService(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("isAuthenticated()")
    fun getServicesByCategory(@PathVariable categoryId: Long): ResponseEntity<List<Services>> {
        val category = ServiceCategory(id = categoryId, name = "")
        val services = servicesService.getServicesByCategory(category)
        return ResponseEntity.ok(services)
    }

    @GetMapping("/category/name/{categoryName}")
    @PreAuthorize("isAuthenticated()")
    fun getServicesByCategoryName(@PathVariable categoryName: String): ResponseEntity<List<Services>> {
        val services = servicesService.getServicesByCategoryName(categoryName)
        return ResponseEntity.ok(services)
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    fun getMyServices(@AuthenticationPrincipal user: UserDetails): ResponseEntity<List<ServiceDTO>> {
        // 1. Get the current provider username
        val providerUsername = user.username

        // 2. Find the provider entity
        val provider = serviceProviderRepository.findByUsername(providerUsername)
            ?: return ResponseEntity.notFound().build()

        // 3. Fetch services for this provider
        val services = servicesService.getServicesByProvider(provider.id!!)
            .map { it.toDto() }

        return ResponseEntity.ok(services)
    }
}