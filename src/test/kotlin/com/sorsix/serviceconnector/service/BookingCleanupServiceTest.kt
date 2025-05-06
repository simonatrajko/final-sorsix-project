package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.*
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.service.impl.BookingCleanupServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlin.test.assertEquals

class BookingCleanupServiceTest {

    private lateinit var bookingRepository: BookingRepository
    private lateinit var slotRepository: ScheduleSlotRepository
    private lateinit var clock: Clock
    private lateinit var service: BookingCleanupServiceImpl

    @BeforeEach
    fun setup() {
        bookingRepository = mock()
        slotRepository = mock()
        clock = mock()
        service = BookingCleanupServiceImpl(bookingRepository, slotRepository, clock)
    }

    @Test
    fun `should cancel expired bookings and free up slots`() {
        val now = Instant.parse("2025-05-01T12:00:00Z")
        val threshold = now.minusSeconds(24 * 3600)

        val slot = ScheduleSlot(
            id = 1L,
            start_time = now.minusSeconds(3600),
            end_time = now,
            slot_id = 100,
            status = Status.BOOKED,
            created_at = now.minusSeconds(3600),
            provider = mock()
        )

        val booking = Booking(
            id = 1L,
            createdAt = threshold.minusSeconds(1), // expired
            client = mock(),
            provider = mock(),
            service = mock(),
            slot = slot,
            status = BookingStatus.PENDING
        )

        whenever(clock.instant()).thenReturn(now)
        whenever(clock.zone).thenReturn(ZoneOffset.UTC)

        whenever(
            bookingRepository.findAllByStatusAndCreatedAtBefore(
                eq(BookingStatus.PENDING),
                eq(threshold)
            )
        ).thenReturn(listOf(booking))

        // Run the service
        service.cancelExpiredPendingBookings()

        // Capture what was saved
        val slotCaptor = argumentCaptor<ScheduleSlot>()
        verify(slotRepository).save(slotCaptor.capture())
        assertEquals(Status.AVAILABLE, slotCaptor.firstValue.status)

        verify(bookingRepository).save(booking)
        assertEquals(BookingStatus.CANCELLED, booking.status)
    }

    @Test
    fun `should do nothing if there are no expired bookings`() {
        val now = Instant.parse("2025-05-01T12:00:00Z")
        val threshold = now.minusSeconds(24 * 3600)

        whenever(clock.instant()).thenReturn(now)
        whenever(clock.zone).thenReturn(ZoneOffset.UTC)

        whenever(
            bookingRepository.findAllByStatusAndCreatedAtBefore(
                eq(BookingStatus.PENDING),
                eq(threshold)
            )
        ).thenReturn(emptyList())

        service.cancelExpiredPendingBookings()

        verify(slotRepository, never()).save(any())
        verify(bookingRepository, never()).save(any())
    }
}
