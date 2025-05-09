package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.model.Status

interface ServiceProviderService {

    fun findByUsername(username: String): ServiceProvider?
    fun getAllServicesForProvider(providerId: Long): List<Services>
    fun getAvailableSlots(providerId: Long): List<ScheduleSlot>
    fun searchProvidersByName(name: String): List<ServiceProvider>
    fun getProvider(providerId: Long): ServiceProvider
}