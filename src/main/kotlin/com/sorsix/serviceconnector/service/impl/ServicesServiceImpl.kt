package com.sorsix.serviceconnector.service.impl

import com.sorsix.serviceconnector.DTO.ServiceDTO
import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.repository.ServiceProviderRepository
import com.sorsix.serviceconnector.repository.ServiceRepository
import com.sorsix.serviceconnector.mapper.toDto
import com.sorsix.serviceconnector.service.ScheduleSlotService
import com.sorsix.serviceconnector.service.ServicesService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

@Service
class ServicesServiceImpl(
    private val serviceRepository: ServiceRepository,
    private val serviceProviderRepository: ServiceProviderRepository,
    private val scheduleSlotService: ScheduleSlotService
) : ServicesService {
    override fun getServiceById(id: Long): Services =
        serviceRepository.findById(id).orElseThrow { RuntimeException("Service not found") }

    override fun getAllServices(pageable: Pageable): Page<Services> =
        serviceRepository.findAll(pageable)


    override fun getAvailableServicesAsDto(pageable: Pageable): Page<ServiceDTO> {
        val page = getAllServices(pageable)

        val filtered = page.content.filter { service ->
            scheduleSlotService.getAvailableSlotsForProvider(service.provider.id).isNotEmpty()
        }

        val dtos = filtered.map { it.toDto() }

        return PageImpl(dtos, pageable, dtos.size.toLong())
    }


    override fun getServicesByCategory(category: ServiceCategory): List<Services> =
        serviceRepository.findAllByCategory(category)

    override fun getServicesByCategoryName(categoryName: String): List<Services> =
        serviceRepository.findAllByCategory_NameIgnoreCase(categoryName)

    override fun getServicesByProvider(providerId: Long): List<Services> =
        serviceRepository.findAllByProvider_Id(providerId)

    override fun createService(service: Services): Services {
        val provider = serviceProviderRepository.findById(service.provider.id!!)
            .orElseThrow({ RuntimeException("Provider not found") })
        return serviceRepository.save(service)
    }

    override fun deleteService(id: Long) =
        serviceRepository.deleteById(id)

    override fun getService(id: Long): Services =
        serviceRepository.findById(id).orElseThrow { RuntimeException("Service not found") }

    override fun updatePrice(serviceId: Long, newPrice: BigDecimal, providerId: Long): Services {
        val service = serviceRepository.findById(serviceId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found") }

        if (service.provider.id != providerId)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update another provider's service")

        val updated = service.copy(price = newPrice)
        return serviceRepository.save(updated)
    }


}