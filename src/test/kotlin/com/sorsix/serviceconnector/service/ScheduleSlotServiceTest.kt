package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.repository.ServiceRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.Optional
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class ScheduleSlotServiceTest {

    @Mock
    lateinit var scheduleSlotRepository: ScheduleSlotRepository

    @Mock
    lateinit var serviceRepository: ServiceRepository

    @InjectMocks
    lateinit var scheduleSlotService: ScheduleSlotService

    private lateinit var provider: ServiceProvider
    private lateinit var slot: ScheduleSlot
    private lateinit var service: Services

    @BeforeEach
    fun setup() {
        provider = ServiceProvider(
            username = "elena",
            hashedPassword =  "1234",
            email = "elena@provider.com",
            fullName = "Elena",
            profileImage = "",
            location = "Skopje",
            yearsOfExperience = 5,
            bio = "Experienced cleaner",
            languages = "Macedonian"
        ).apply { id = 1L }

        val category = ServiceCategory(id = 1L, name = "Cleaning")

        service = Services(
            id = 2L,
            title = "Deep Clean",
            description = "Full apartment cleaning",
            price = BigDecimal(1500),
            duration = 90,
            category = category,
            createdAt = LocalDateTime.now(),
            provider = provider
        )

        slot = ScheduleSlot(
            id = 1L,
            start_time = Instant.now().plusSeconds(3600),
            end_time = Instant.now().plusSeconds(7200),
            slot_id = 1L,
            created_at = Instant.now(),
            status = Status.AVAILABLE,
            provider = provider
        )
    }

    @Test
    fun `getAllAvailableSlots should return only available slots`() {
        val expectedSlots = listOf(slot)
        Mockito.`when`(scheduleSlotRepository.findAllByStatus(Status.AVAILABLE)).thenReturn(expectedSlots)

        val result = scheduleSlotService.getAllAvailableSlots()

        assertEquals(expectedSlots, result)
        Mockito.verify(scheduleSlotRepository).findAllByStatus(Status.AVAILABLE)
    }

    @Test
    fun `getAvailableSlotsForProvider should return available slots for given provider`() {
        val expectedSlots = listOf(slot)
        Mockito.`when`(scheduleSlotRepository.findAllByProvider_IdAndStatus(provider.id, Status.AVAILABLE)).thenReturn(expectedSlots)

        val result = scheduleSlotService.getAvailableSlotsForProvider(provider.id)

        assertEquals(expectedSlots, result)
        Mockito.verify(scheduleSlotRepository).findAllByProvider_IdAndStatus(provider.id, Status.AVAILABLE)
    }

    @Test
    fun `getAvailableSlotsForService should return available slots for given service`() {
        val expectedSlots = listOf(slot)
        Mockito.`when`(scheduleSlotRepository.findAllByProvider_IdAndStatus(service.provider.id, Status.AVAILABLE)).thenReturn(expectedSlots)
        Mockito.`when`(serviceRepository.findById(service.id!!)).thenReturn(java.util.Optional.of(service))

        val result = scheduleSlotService.getAvailableSlotsForService(service.id!!)

        assertEquals(expectedSlots, result)
        Mockito.verify(serviceRepository).findById(service.id!!)
        Mockito.verify(scheduleSlotRepository).findAllByProvider_IdAndStatus(service.provider.id, Status.AVAILABLE)
    }
    @Test
    fun `markSlotAsBooked should change status to BOOKED and save`() {
        val bookedSlot = slot.copy(status = Status.BOOKED)
        Mockito.`when`(scheduleSlotRepository.findById(slot.id!!)).thenReturn(Optional.of(slot))
        Mockito.`when`(scheduleSlotRepository.save(Mockito.any())).thenReturn(bookedSlot)

        val result = scheduleSlotService.markSlotAsBooked(slot.id!!)

        assertEquals(Status.BOOKED, result.status)
        Mockito.verify(scheduleSlotRepository).findById(slot.id!!)
        Mockito.verify(scheduleSlotRepository).save(Mockito.any())
    }

    @Test
    fun `createSlot should save and return the created slot`() {
        Mockito.`when`(scheduleSlotRepository.save(slot)).thenReturn(slot)

        val result = scheduleSlotService.createSlot(slot)

        assertEquals(slot, result)
        Mockito.verify(scheduleSlotRepository).save(slot)
    }

    @Test
    fun `deleteSlot should call deleteById on repository`() {
        scheduleSlotService.deleteSlot(slot.id!!)
        Mockito.verify(scheduleSlotRepository).deleteById(slot.id!!)
    }
}
