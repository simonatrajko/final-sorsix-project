package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.*
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class BookingServiceTest {

    @Mock
    private lateinit var bookingRepository: BookingRepository

    @Mock
    private lateinit var scheduleSlotRepository: ScheduleSlotRepository

    @InjectMocks
    private lateinit var bookingService: BookingService

    @Captor
    private lateinit var bookingCaptor: ArgumentCaptor<Booking>

    @Captor
    private lateinit var slotCaptor: ArgumentCaptor<ScheduleSlot>

    private lateinit var seeker: ServiceSeeker
    private lateinit var provider: ServiceProvider
    private lateinit var service: Services
    private lateinit var slot: ScheduleSlot
    private lateinit var booking: Booking

    @BeforeEach
    fun setUp() {
        seeker = ServiceSeeker("testseeker", "password", "seeker@test.com", "Test Seeker", "", "Test Location")
        seeker.id = 1L

        provider =
            ServiceProvider("testprovider", "password", "provider@test.com", "Test Provider", "", "Test Location", 5)
        provider.id = 2L

        val category = ServiceCategory(1L, "Test Category")

        service = Services(
            1L,
            "Test Service",
            "Test Description",
            BigDecimal("100.00"),
            60,
            category,
            LocalDateTime.now(),
            provider
        )

        slot = ScheduleSlot(
            1L,
            Instant.now().plusSeconds(3600),
            Instant.now().plusSeconds(7200),
            1L,
            Status.AVAILABLE,
            Instant.now(),
            provider
        )

        booking =
            Booking(1L, Instant.now(), seeker, provider, service, slot, BookingStatus.PENDING, isRecurring = false)
    }

    @Test
    fun `createBooking should create and save a booking when all validations pass`() {
        `when`(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findBySlotId(1L)).thenReturn(emptyList())
        `when`(bookingRepository.findByClientId(seeker.id)).thenReturn(emptyList())
        `when`(bookingRepository.save(any())).thenReturn(booking)

        val result = bookingService.createBooking(seeker, service, 1L, isRecurring = false)

        verify(bookingRepository).save(any())
        assertEquals(BookingStatus.PENDING, result.status)
        assertEquals(seeker, result.client)
        assertEquals(provider, result.provider)
        assertEquals(service, result.service)
        assertEquals(slot, result.slot)
    }

    @Test
    fun `createBooking should save booking with isRecurring true`() {
        val slotId = 1L

        val recurringBooking = booking.copy(isRecurring = true)

        `when`(scheduleSlotRepository.findById(slotId)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findBySlotId(slotId)).thenReturn(emptyList())
        `when`(bookingRepository.findByClientId(seeker.id)).thenReturn(emptyList())
        `when`(bookingRepository.save(any())).thenReturn(recurringBooking)

        val result = bookingService.createBooking(seeker, service, slotId, isRecurring = true)

        verify(bookingRepository).save(any())
        assertEquals(true, result.isRecurring)
        assertEquals(BookingStatus.PENDING, result.status)
    }

    @Test
    fun `createBooking should throw exception when slot not found`() {
        `when`(scheduleSlotRepository.findById(999L)).thenReturn(Optional.empty())

        assertThrows(IllegalArgumentException::class.java) {
            bookingService.createBooking(seeker, service, 999L, isRecurring = false)
        }
        verify(bookingRepository, never()).save(any())
    }


    @Test
    fun `completeBooking should create new slot 7 days later for recurring bookings`() {
        booking.isRecurring = true
        val bookingId = booking.id!!
        val originalStart = slot.start_time
        val originalEnd = slot.end_time

        val newSlot = slot.copy(
            id = 2L,
            start_time = originalStart.plusSeconds(7 * 24 * 3600),
            end_time = originalEnd.plusSeconds(7 * 24 * 3600)
        )

        `when`(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking))
        `when`(bookingRepository.save(any())).thenReturn(booking.copy(status = BookingStatus.COMPLETED))
        `when`(scheduleSlotRepository.save(any())).thenReturn(newSlot)

        val result = bookingService.completeBooking(bookingId)

        verify(scheduleSlotRepository).save(any())

        verify(bookingRepository, times(2)).save(any())

        assertEquals(BookingStatus.COMPLETED, result.status)
    }

    @Test
    fun `createBooking should throw exception when slot is not available`() {
        val bookedSlot = slot.copy(status = Status.BOOKED)
        `when`(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(bookedSlot))

        assertThrows(IllegalStateException::class.java) {
            bookingService.createBooking(seeker, service, 1L, isRecurring = false)
        }
        verify(bookingRepository, never()).save(any())
    }

    @Test
    fun `createBooking should throw exception when client already has booking for slot`() {
        `when`(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(seeker.id)).thenReturn(listOf(booking))

        assertThrows(IllegalStateException::class.java) {
            bookingService.createBooking(seeker, service, 1L, isRecurring = false)
        }
        verify(bookingRepository, never()).save(any())
    }

    @Test
    fun `createBooking should throw exception when slot already has confirmed booking`() {
        val confirmedBooking = booking.copy(status = BookingStatus.CONFIRMED)
        `when`(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(seeker.id)).thenReturn(emptyList())
        `when`(bookingRepository.findBySlotId(1L)).thenReturn(listOf(confirmedBooking))

        assertThrows(IllegalStateException::class.java) {
            bookingService.createBooking(seeker, service, 1L, isRecurring = false)
        }
        verify(bookingRepository, never()).save(any())
    }

    @Test
    fun `respondToBooking should confirm booking and update slot status when accepted`() {
        `when`(bookingRepository.findById(1L)).thenReturn(Optional.of(booking))
        `when`(scheduleSlotRepository.save(any())).thenReturn(slot.copy(status = Status.BOOKED))
        `when`(bookingRepository.save(any())).thenReturn(
            booking.copy(
                status = BookingStatus.CONFIRMED,
                slot = slot.copy(status = Status.BOOKED)
            )
        )

        val result = bookingService.respondToBooking(1L, true)

        assertEquals(BookingStatus.CONFIRMED, result.status)
        assertEquals(Status.BOOKED, result.slot.status)
    }

    @Test
    fun `respondToBooking should reject booking without changing slot status when rejected`() {
        `when`(bookingRepository.findById(1L)).thenReturn(Optional.of(booking))
        `when`(bookingRepository.save(any())).thenReturn(booking.copy(status = BookingStatus.REJECTED))

        val result = bookingService.respondToBooking(1L, false)

        assertEquals(BookingStatus.REJECTED, result.status)
        assertEquals(Status.AVAILABLE, result.slot.status)
    }
}
