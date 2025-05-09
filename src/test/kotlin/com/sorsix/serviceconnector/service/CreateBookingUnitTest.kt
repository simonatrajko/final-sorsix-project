package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.*
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.service.impl.BookingServiceImpl
import com.sorsix.serviceconnector.service.impl.ScheduleSlotServiceImpl
import com.sorsix.serviceconnector.service.impl.ServiceSeekerServiceImpl
import com.sorsix.serviceconnector.service.impl.ServicesServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@ExtendWith(MockitoExtension::class)
class CreateBookingUnitTest {

    private lateinit var bookingService: BookingServiceImpl
    private lateinit var bookingRepository: BookingRepository
    private lateinit var scheduleSlotRepository: ScheduleSlotRepository
    private lateinit var serviceSeekerService: ServiceSeekerServiceImpl
    private lateinit var scheduleSlotService: ScheduleSlotServiceImpl
    private lateinit var servicesService: ServicesServiceImpl

    private lateinit var seeker: ServiceSeeker
    private lateinit var provider: ServiceProvider
    private lateinit var service: Services
    private lateinit var slot: ScheduleSlot

    @BeforeEach
    fun setup() {
        bookingRepository = mock(BookingRepository::class.java)
        scheduleSlotRepository = mock(ScheduleSlotRepository::class.java)
        serviceSeekerService = mock(ServiceSeekerServiceImpl::class.java)
        scheduleSlotService = mock(ScheduleSlotServiceImpl::class.java)
        servicesService = mock(ServicesServiceImpl::class.java)

        bookingService = BookingServiceImpl(
            bookingRepository,
            scheduleSlotRepository,
            serviceSeekerService,
            scheduleSlotService,
            servicesService
        )

        seeker = ServiceSeeker("seeker", "pass", "mail", "Name", "", "Skopje").apply { id = 1L }
        provider = ServiceProvider("provider", "pass", "mail", "Name", "", "Skopje", 5).apply { id = 2L }

        service = Services(
            id = 10L,
            title = "Test Service",
            description = "Desc",
            price = BigDecimal(100),
            duration = 60,
            category = ServiceCategory(1, "Cleaning"),
            createdAt = LocalDateTime.now(),
            provider = provider
        )

        slot = ScheduleSlot(
            id = 1L,
            start_time = LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC),
            end_time = LocalDateTime.now().plusDays(1).plusHours(1).toInstant(ZoneOffset.UTC),
            slot_id = 100,
            status = Status.AVAILABLE,
            created_at = Instant.now(),
            provider = provider
        )
    }

    @Test
    fun `should create booking successfully`() {
        `when`(serviceSeekerService.findByUsername("seeker")).thenReturn(seeker)
        `when`(servicesService.getServiceById(10L)).thenReturn(service)
        `when`(scheduleSlotService.getSlotById(1L)).thenReturn(slot)
        `when`(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(1L)).thenReturn(emptyList())
        `when`(bookingRepository.findBySlotId(1L)).thenReturn(emptyList())

        val saved = bookingService.handleCreateBookingRequest(10L, 1L, false, "seeker")

        assertEquals(BookingStatus.PENDING, saved.status)
        assertEquals(seeker, saved.client)
        assertEquals(service, saved.service)
        assertEquals(slot, saved.slot)
    }

    @Test
    fun `should fail if slot does not belong to service provider`() {
        val wrongProvider = provider.copy(id = 99L)
        val wrongSlot = slot.copy(provider = wrongProvider)

        `when`(serviceSeekerService.findByUsername("seeker")).thenReturn(seeker)
        `when`(servicesService.getServiceById(10L)).thenReturn(service)
        `when`(scheduleSlotService.getSlotById(1L)).thenReturn(wrongSlot)

        val ex = assertThrows(IllegalStateException::class.java) {
            bookingService.handleCreateBookingRequest(10L, 1L, false, "seeker")
        }
        assertTrue(ex.message!!.contains("Slot does not belong"))
    }

    @Test
    fun `should create recurring booking`() {
        `when`(serviceSeekerService.findByUsername("seeker")).thenReturn(seeker)
        `when`(servicesService.getServiceById(10L)).thenReturn(service)
        `when`(scheduleSlotService.getSlotById(1L)).thenReturn(slot)
        `when`(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(1L)).thenReturn(emptyList())
        `when`(bookingRepository.findBySlotId(1L)).thenReturn(emptyList())

        val result = bookingService.handleCreateBookingRequest(10L, 1L, true, "seeker")

        assertTrue(result.isRecurring)
    }

    @Test
    fun `should fail if slot is not available`() {
        val unavailable = slot.copy(status = Status.BOOKED)
        `when`(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(unavailable))

        val ex = assertThrows(IllegalStateException::class.java) {
            bookingService.createBooking(seeker, service, 1L, false)
        }
        assertTrue(ex.message!!.contains("Slot not available"))
    }

    @Test
    fun `should fail if client has already booked same slot`() {
        val existingBooking = Booking(1, Instant.now(), seeker, provider, service, slot, BookingStatus.PENDING, false)
        `when`(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(1L)).thenReturn(listOf(existingBooking))

        val ex = assertThrows(IllegalStateException::class.java) {
            bookingService.createBooking(seeker, service, 1L, false)
        }
        assertTrue(ex.message!!.contains("already has booking"))
    }

    @Test
    fun `should fail if slot already has confirmed booking`() {
        val confirmed = Booking(1, Instant.now(), seeker, provider, service, slot, BookingStatus.CONFIRMED, false)
        `when`(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(1L)).thenReturn(emptyList())
        `when`(bookingRepository.findBySlotId(1L)).thenReturn(listOf(confirmed))

        val ex = assertThrows(IllegalStateException::class.java) {
            bookingService.createBooking(seeker, service, 1L, false)
        }
        assertTrue(ex.message!!.contains("already confirmed"))
    }
}
