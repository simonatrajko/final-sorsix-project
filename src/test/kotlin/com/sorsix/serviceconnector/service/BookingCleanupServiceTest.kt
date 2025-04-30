package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.Booking
import com.sorsix.serviceconnector.model.BookingStatus
import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.model.ServiceSeeker
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.service.impl.BookingCleanupService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class BookingCleanupServiceTest {

    @Mock
    private lateinit var bookingRepository: BookingRepository

    @Mock
    private lateinit var scheduleSlotRepository: ScheduleSlotRepository

    @Mock
    private lateinit var clock: Clock

    @InjectMocks
    private lateinit var bookingCleanupService: BookingCleanupService

    @Captor
    private lateinit var bookingCaptor: ArgumentCaptor<Booking>

    @Captor
    private lateinit var slotCaptor: ArgumentCaptor<ScheduleSlot>

    private lateinit var expiredBooking: Booking
    private lateinit var expiredBookingWithBookedSlot: Booking
    private lateinit var slot: ScheduleSlot
    private lateinit var bookedSlot: ScheduleSlot

    @BeforeEach
    fun setUp() {

        // Configure the clock to return a fixed time
        val fixedInstant = Instant.parse("2025-04-23T15:45:49.632Z")
        `when`(clock.instant()).thenReturn(fixedInstant)
        `when`(clock.getZone()).thenReturn(ZoneId.of("UTC"))

        // Create a mock slot
        slot = mock(ScheduleSlot::class.java)
        `when`(slot.status).thenReturn(Status.AVAILABLE)
        `when`(slot.id).thenReturn(1L)

        // Create a mock booked slot
        bookedSlot = mock(ScheduleSlot::class.java)
        `when`(bookedSlot.status).thenReturn(Status.BOOKED)
        `when`(bookedSlot.id).thenReturn(2L)

        // Create a copy method for the bookedSlot mock
        `when`(bookedSlot.copy(status = Status.AVAILABLE)).thenReturn(bookedSlot)

        // Create a mock expired booking with available slot
        expiredBooking = mock(Booking::class.java)
        `when`(expiredBooking.id).thenReturn(1L)
        `when`(expiredBooking.status).thenReturn(BookingStatus.PENDING)
        `when`(expiredBooking.slot).thenReturn(slot)

        // Create a mock expired booking with booked slot
        expiredBookingWithBookedSlot = mock(Booking::class.java)
        `when`(expiredBookingWithBookedSlot.id).thenReturn(2L)
        `when`(expiredBookingWithBookedSlot.status).thenReturn(BookingStatus.PENDING)
        `when`(expiredBookingWithBookedSlot.slot).thenReturn(bookedSlot)
    }

    @Test
    fun `should cancel expired pending bookings`() {
        val now = Instant.parse("2025-04-24T12:00:00Z")
        val threshold = now.minusSeconds(24 * 3600)

        // mock clock to always return the same time
        whenever(clock.instant()).thenReturn(now)
        whenever(clock.zone).thenReturn(ZoneId.of("UTC"))

        val slot = ScheduleSlot(
            id = 1L,
            start_time = Instant.now(),
            end_time = Instant.now().plusSeconds(3600),
            slot_id = 1L,
            status = Status.BOOKED,
            created_at = Instant.now(),
            provider = ServiceProvider("p", "p", "p", "p", "", "l", 1).apply { id = 1L }
        )

        val expiredBooking = Booking(
            id = 1L,
            createdAt = threshold.minusSeconds(10),
            client = ServiceSeeker("s", "p", "e", "f", "", "l").apply { id = 2L },
            provider = slot.provider as ServiceProvider,
            service = Services(
                1L, "t", "d", BigDecimal.ONE, 30,
                ServiceCategory(1L, "c"), LocalDateTime.now(), slot.provider as ServiceProvider
            ),
            slot = slot,
            status = BookingStatus.PENDING,
            isRecurring = false
        )

        whenever(bookingRepository.findAllByStatusAndCreatedAtBefore(eq(BookingStatus.PENDING), eq(threshold)))
            .thenReturn(listOf(expiredBooking))
        whenever(bookingRepository.save(any())).thenAnswer { it.arguments[0] }
        whenever(scheduleSlotRepository.save(any())).thenAnswer { it.arguments[0] }

        // Act
        bookingCleanupService.cancelExpiredPendingBookings()

        // Assert
        verify(bookingRepository).save(expiredBooking)
        verify(scheduleSlotRepository).save(argThat { it.status == Status.AVAILABLE })
    }



    @Test
    fun `should free up booked slots when cancelling expired bookings`() {
        // Arrange
        val expiredBookings = listOf(expiredBookingWithBookedSlot)

        doReturn(expiredBookings).`when`(bookingRepository)
            .findAllByStatusAndCreatedAtBefore(BookingStatus.PENDING, Instant.now().minusSeconds(24 * 3600))

        doReturn(expiredBookingWithBookedSlot).`when`(bookingRepository).save(expiredBookingWithBookedSlot)
        doReturn(bookedSlot).`when`(scheduleSlotRepository).save(bookedSlot)

        // Act
        bookingCleanupService.cancelExpiredPendingBookings()

        // Assert
        verify(bookingRepository).save(bookingCaptor.capture())
        assertEquals(BookingStatus.CANCELLED, bookingCaptor.value.status)

        verify(scheduleSlotRepository).save(slotCaptor.capture())
    }

    @Test
    fun `should handle empty list of expired bookings`() {
        // Arrange
        doReturn(emptyList<Booking>()).`when`(bookingRepository)
            .findAllByStatusAndCreatedAtBefore(BookingStatus.PENDING, Instant.now().minusSeconds(24 * 3600))

        // Act
        bookingCleanupService.cancelExpiredPendingBookings()

        // Assert
        verify(bookingRepository, never()).save(any())
        verify(scheduleSlotRepository, never()).save(any())
    }

    @Test
    fun `should handle exceptions when fetching expired bookings`() {
        // Arrange
        doThrow(RuntimeException("Database connection failed")).`when`(bookingRepository)
            .findAllByStatusAndCreatedAtBefore(BookingStatus.PENDING, Instant.now().minusSeconds(24 * 3600))

        // Act - should not throw exception
        bookingCleanupService.cancelExpiredPendingBookings()

        // Assert - no interactions should happen after exception
        verify(bookingRepository, never()).save(any())
        verify(scheduleSlotRepository, never()).save(any())
    }

    @Test
    fun `should continue processing bookings when one fails`() {
        // Arrange
        val goodBooking = expiredBooking
        val badBooking = mock(Booking::class.java)

        // Set up the bad booking to throw an exception when saved
        `when`(badBooking.id).thenReturn(3L)
        `when`(badBooking.status).thenReturn(BookingStatus.PENDING)
        `when`(badBooking.slot).thenReturn(slot)

        val expiredBookings = listOf(badBooking, goodBooking)

        doReturn(expiredBookings).`when`(bookingRepository)
            .findAllByStatusAndCreatedAtBefore(BookingStatus.PENDING, Instant.now().minusSeconds(24 * 3600))

        doThrow(RuntimeException("Failed to save")).`when`(bookingRepository).save(badBooking)
        doReturn(goodBooking).`when`(bookingRepository).save(goodBooking)

        // Act
        bookingCleanupService.cancelExpiredPendingBookings()

        // Assert
        // Verify that the service attempted to save both bookings
        verify(bookingRepository).save(badBooking)
        verify(bookingRepository).save(goodBooking)

        // Even though one booking failed, the other was processed
        verify(bookingRepository, times(2)).save(any())
    }
}