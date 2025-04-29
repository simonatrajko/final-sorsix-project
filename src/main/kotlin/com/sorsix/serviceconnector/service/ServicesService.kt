package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.DTO.ServiceDTO
import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.repository.ServiceProviderRepository
import com.sorsix.serviceconnector.repository.ServiceRepository
import com.sorsix.serviceconnector.mapper.toDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ServicesService(
    private val serviceRepository: ServiceRepository,
    private val serviceProviderRepository: ServiceProviderRepository,
    private val scheduleSlotService: ScheduleSlotService
) {
    fun getServiceById(id: Long): Services =
        serviceRepository.findById(id).orElseThrow { RuntimeException("Service not found") }

    fun getAllServices(pageable: Pageable): Page<Services> =
        serviceRepository.findAll(pageable)


    fun getAvailableServicesAsDto(pageable: Pageable): Page<ServiceDTO> {
        val page = getAllServices(pageable)

        val filtered = page.content.filter { service ->
            scheduleSlotService.getAvailableSlotsForProvider(service.provider.id).isNotEmpty()
        }

        val dtos = filtered.map { it.toDto() }

        return PageImpl(dtos, pageable, dtos.size.toLong())
    }


    fun getServicesByCategory(category: ServiceCategory): List<Services> =
        serviceRepository.findAllByCategory(category)

    fun getServicesByCategoryName(categoryName: String): List<Services> =
        serviceRepository.findAllByCategory_NameIgnoreCase(categoryName)

    fun getServicesByProvider(providerId: Long): List<Services> =
        serviceRepository.findAllByProvider_Id(providerId)

    fun createService(service: Services): Services {
        // Example validation, ако имаш провајдер сервис
        val provider = serviceProviderRepository.findById(service.provider.id!!)
            .orElseThrow({ RuntimeException("Provider not found") })
        return serviceRepository.save(service)
    }

    fun deleteService(id: Long) =
        serviceRepository.deleteById(id)

    fun getService(id: Long): Services =
        serviceRepository.findById(id).orElseThrow { RuntimeException("Service not found") }
}