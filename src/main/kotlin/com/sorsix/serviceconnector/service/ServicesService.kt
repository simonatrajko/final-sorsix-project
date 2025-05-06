package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.DTO.ServiceDTO
import com.sorsix.serviceconnector.mapper.toDto
import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.model.Services
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.math.BigDecimal

interface ServicesService {
    fun getServiceById(id: Long): Services
    fun getAllServices(pageable: Pageable): Page<Services>
    fun getAvailableServicesAsDto(pageable: Pageable): Page<ServiceDTO>
    fun getServicesByCategory(category: ServiceCategory): List<Services>
    fun getServicesByCategoryName(categoryName: String): List<Services>
    fun getServicesByProvider(providerId: Long): List<Services>
    fun createService(service: Services): Services
    fun deleteService(id: Long)
    fun getService(id: Long): Services
    fun updatePrice(serviceId: Long, newPrice: BigDecimal, providerId: Long): Services}