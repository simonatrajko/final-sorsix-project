package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.DTO.ServiceDTO
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.service.ScheduleSlotService
import com.sorsix.serviceconnector.service.ServicesService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/services")
class ServiceController(
    private val servicesService: ServicesService,
    private val scheduleSlotService: ScheduleSlotService
) {
    @GetMapping
    fun getAllAvailableServices(pageable: Pageable): Page<ServiceDTO> =
        servicesService.getAvailableServicesAsDto(pageable)
}