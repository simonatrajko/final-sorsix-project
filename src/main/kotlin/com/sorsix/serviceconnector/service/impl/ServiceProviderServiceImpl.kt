package com.sorsix.serviceconnector.service.impl

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.repository.ServiceProviderRepository
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.repository.ServiceRepository
import com.sorsix.serviceconnector.service.ServiceProviderService
import org.springframework.stereotype.Service

@Service
class ServiceProviderServiceImpl(
    private val serviceProviderRepository: ServiceProviderRepository,
    private val servicesRepository: ServiceRepository,
    private val scheduleSlotRepository: ScheduleSlotRepository
) : ServiceProviderService {

    override fun findByUsername(username: String): ServiceProvider? =
        serviceProviderRepository.findByUsername(username)

    override fun getAllServicesForProvider(providerId: Long): List<Services> =
        servicesRepository.findAllByProvider_Id(providerId)

    override fun getAvailableSlots(providerId: Long): List<ScheduleSlot> =
        scheduleSlotRepository.findAllByProvider_IdAndStatus(providerId, Status.AVAILABLE)

    override fun searchProvidersByName(name: String): List<ServiceProvider> =
        serviceProviderRepository.findByFullNameContainingIgnoreCase(name)

    override fun getProvider(id: Long): ServiceProvider =
        serviceProviderRepository.findById(id)
            .orElseThrow { NoSuchElementException("Provider not found") }
}