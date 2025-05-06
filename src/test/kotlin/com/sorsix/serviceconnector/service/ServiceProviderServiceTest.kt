package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.repository.ServiceProviderRepository
import com.sorsix.serviceconnector.repository.ServiceRepository
import com.sorsix.serviceconnector.service.impl.ServiceProviderServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.Test


@ExtendWith(MockitoExtension::class)
class ServiceProviderServiceTest {

    @Mock
    lateinit var serviceProviderRepository: ServiceProviderRepository

    @Mock
    lateinit var serviceRepository: ServiceRepository

    @Mock
    lateinit var scheduleSlotRepository: ScheduleSlotRepository

    @InjectMocks
    lateinit var serviceProviderService: ServiceProviderServiceImpl

    private val provider = ServiceProvider(
        username = "provider1",
        hashedPassword =  "pass",
        email = "p@mail.com",
        fullName = "Provider One",
        profileImage = "",
        location = "Skopje",
        yearsOfExperience = 5
    ).apply { id = 1L }

    @Test
    fun `findByUsername should return provider`() {
        whenever(serviceProviderRepository.findByUsername("provider1")).thenReturn(provider)
        val result = serviceProviderService.findByUsername("provider1")
        assertEquals(provider, result)
    }

    @Test
    fun `getAllServicesForProvider should return services list`() {
        val services = listOf(mock<Services>())
        whenever(serviceRepository.findAllByProvider_Id(1L)).thenReturn(services)
        val result = serviceProviderService.getAllServicesForProvider(1L)
        assertEquals(services, result)
    }

    @Test
    fun `getAvailableSlots should return available slots`() {
        val slots = listOf(mock<ScheduleSlot>())
        whenever(scheduleSlotRepository.findAllByProvider_IdAndStatus(1L, Status.AVAILABLE)).thenReturn(slots)
        val result = serviceProviderService.getAvailableSlots(1L)
        assertEquals(slots, result)
    }
}

