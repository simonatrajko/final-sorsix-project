package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.*
import com.sorsix.serviceconnector.repository.ServiceProviderRepository
import com.sorsix.serviceconnector.repository.ServiceRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class ServicesServiceTest {

    @Mock
    lateinit var serviceRepository: ServiceRepository

    @Mock
    lateinit var serviceProviderRepository: ServiceProviderRepository

    @Mock
    lateinit var scheduleSlotService: ScheduleSlotService

    @InjectMocks
    lateinit var servicesService: ServicesService

    private lateinit var provider: ServiceProvider
    private lateinit var category: ServiceCategory
    private lateinit var service: Services

    @BeforeEach
    fun setup() {
        provider = ServiceProvider(
            username = "provider1",
            password = "secret",
            email = "provider@mail.com",
            fullName = "Provider One",
            profileImage = "",
            location = "Skopje",
            yearsOfExperience = 3,
            bio = "Bio",
            languages = "Macedonian"
        )
        provider.id = 1L

        category = ServiceCategory(
            id = 1L,
            name = "Cleaning"
        )

        service = Services(
            id = 10L,
            title = "Apartment Cleaning",
            description = "Deep cleaning",
            price = BigDecimal(1200),
            duration = 90,
            category = category,
            createdAt = LocalDateTime.now(),
            provider = provider
        )
    }


    @Test
    fun `getAllServices should return all services from repository`() {
        val pageable: Pageable = PageRequest.of(0, 10)
        val servicesList = listOf(service)
        val servicePage = PageImpl(servicesList, pageable, servicesList.size.toLong())

        Mockito.`when`(serviceRepository.findAll(pageable)).thenReturn(servicePage)

        val result = servicesService.getAllServices(pageable)

        assertEquals(servicesList, result.content)
        verify(serviceRepository).findAll(pageable)
    }

    @Test
    fun `getServicesByCategory should return services with given category`() {
        val expected = listOf(service)
        Mockito.`when`(serviceRepository.findAllByCategory(category)).thenReturn(expected)

        val result = servicesService.getServicesByCategory(category)

        assertEquals(expected, result)
        verify(serviceRepository).findAllByCategory(category)
    }
    @Test
    fun `getServicesByCategoryName should return services with given category name`() {
        val expected = listOf(service)
        Mockito.`when`(serviceRepository.findAllByCategory_NameIgnoreCase("Cleaning")).thenReturn(expected)

        val result = servicesService.getServicesByCategoryName("Cleaning")

        assertEquals(expected, result)
        verify(serviceRepository).findAllByCategory_NameIgnoreCase("Cleaning")
    }

    @Test
    fun `getServicesByProvider should return services for given provider`() {
        val expected = listOf(service)
        Mockito.`when`(serviceRepository.findAllByProvider_Id(provider.id)).thenReturn(expected)

        val result = servicesService.getServicesByProvider(provider.id)

        assertEquals(expected, result)
        verify(serviceRepository).findAllByProvider_Id(provider.id)
    }

    @Test
    fun `createService should save service when provider exists`() {
        Mockito.`when`(serviceProviderRepository.findById(provider.id)).thenReturn(Optional.of(provider))
        Mockito.`when`(serviceRepository.save(service)).thenReturn(service)

        val result = servicesService.createService(service)

        assertEquals(service, result)
        verify(serviceProviderRepository).findById(provider.id)
        verify(serviceRepository).save(service)
    }

    @Test
    fun `deleteService should call deleteById on repository`() {
        servicesService.deleteService(service.id!!)
        verify(serviceRepository).deleteById(service.id!!)
    }

    @Test
    fun `getAvailableServicesAsDto should return filtered and mapped services`() {
        val pageable = PageRequest.of(0, 10)
        val servicesList = listOf(service)
        val servicePage = PageImpl(servicesList, pageable, servicesList.size.toLong())

        Mockito.`when`(serviceRepository.findAll(pageable)).thenReturn(servicePage)
        Mockito.`when`(scheduleSlotService.getAvailableSlotsForProvider(provider.id)).thenReturn(listOf(mockSlot()))

        val result = servicesService.getAvailableServicesAsDto(pageable)

        assertEquals(1, result.totalElements)
        verify(serviceRepository).findAll(pageable)
        verify(scheduleSlotService).getAvailableSlotsForProvider(provider.id)
    }

    @Test
    fun `getServiceById should return service if found`() {
        Mockito.`when`(serviceRepository.findById(service.id!!)).thenReturn(Optional.of(service))

        val result = servicesService.getServiceById(service.id!!)

        assertEquals(service, result)
        verify(serviceRepository).findById(service.id!!)
    }

    @Test
    fun `getServiceById should throw exception if service not found`() {
        Mockito.`when`(serviceRepository.findById(999L)).thenReturn(Optional.empty())

        assertFailsWith<RuntimeException> {
            servicesService.getServiceById(999L)
        }
        verify(serviceRepository).findById(999L)
    }

    @Test
    fun `getService should return service if found`() {
        Mockito.`when`(serviceRepository.findById(service.id!!)).thenReturn(Optional.of(service))

        val result = servicesService.getService(service.id!!)

        assertEquals(service, result)
        verify(serviceRepository).findById(service.id!!)
    }

    @Test
    fun `getService should throw exception if service not found`() {
        Mockito.`when`(serviceRepository.findById(999L)).thenReturn(Optional.empty())

        assertFailsWith<RuntimeException> {
            servicesService.getService(999L)
        }
        verify(serviceRepository).findById(999L)
    }


    private fun mockSlot(): ScheduleSlot = ScheduleSlot(
        id = 1L,
        start_time = LocalDateTime.now().minusHours(1).toInstant(java.time.ZoneOffset.UTC),
        end_time = LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC),
        slot_id = 1L,
        status = Status.AVAILABLE,
        created_at = LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC),
        provider = provider
    )

}
