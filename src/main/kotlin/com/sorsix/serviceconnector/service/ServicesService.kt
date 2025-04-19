package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.repository.ServiceRepository
import org.springframework.stereotype.Service

@Service
class ServicesService(private val serviceRepository: ServiceRepository) {

    fun getAllServices(): List<Services> =
        serviceRepository.findAll()

    fun getServicesByCategory(category: ServiceCategory): List<Services> =
        serviceRepository.findAllByCategory(category)

    fun getServicesByCategoryName(categoryName: String): List<Services> =
        serviceRepository.findAllByCategory_NameIgnoreCase(categoryName)

    fun getServicesByProvider(providerId: Long): List<Services> =
        serviceRepository.findAllByProvider_Id(providerId)

    fun createService(service: Services): Services =
        serviceRepository.save(service)

    fun deleteService(id: Long) =
        serviceRepository.deleteById(id)

    fun getService(id: Long): Services =
        serviceRepository.findById(id).orElseThrow { RuntimeException("Service not found") }
}