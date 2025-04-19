package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.repository.ServiceProviderRepository
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.repository.ServiceRepository
import org.springframework.stereotype.Service

@Service
class ServiceProviderService(
    private val serviceProviderRepository: ServiceProviderRepository,
    private val servicesRepository: ServiceRepository,
    private val scheduleSlotRepository: ScheduleSlotRepository
) {

    fun findByUsername(username: String): ServiceProvider? =
        serviceProviderRepository.findByUsername(username)

    fun getAllServicesForProvider(providerId: Long): List<Services> =
        servicesRepository.findAllByProvider_Id(providerId)

    fun getAvailableSlots(providerId: Long): List<ScheduleSlot> =
        scheduleSlotRepository.findAllByProvider_IdAndStatus(providerId, Status.AVAILABLE)
}