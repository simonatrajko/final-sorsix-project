package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.Booking
import com.sorsix.serviceconnector.model.BookingStatus
import com.sorsix.serviceconnector.model.ServiceSeeker
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val scheduleSlotRepository: ScheduleSlotRepository
) {

    fun createBooking(
        seeker: ServiceSeeker,
        service: Services,
        slotId: Long
    ): Booking {
        val slot = scheduleSlotRepository.findById(slotId)
            .orElseThrow { IllegalArgumentException("Slot not found") }

        if (slot.status != Status.AVAILABLE) {
            throw IllegalStateException("Slot is not available")
        }

        val booking = Booking(
            createdAt = Instant.now(),
            client = seeker,
            provider = service.provider,
            service = service,
            slot = slot,
            status = BookingStatus.PENDING
        )

        return bookingRepository.save(booking)
    }

    fun respondToBooking(bookingId: Long, accept: Boolean): Booking {
        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { RuntimeException("Booking not found") }

        if (accept) {
            booking.status = BookingStatus.CONFIRMED
            booking.slot.status = Status.BOOKED
            scheduleSlotRepository.save(booking.slot)
        } else {
            booking.status = BookingStatus.REJECTED
        }

        return bookingRepository.save(booking)
    }

    fun getBookingsForProvider(providerId: Long): List<Booking> =
        bookingRepository.findByProviderId(providerId)

    fun getBookingsForSeeker(seekerId: Long): List<Booking> =
        bookingRepository.findByClientId(seekerId)

    fun cancelBooking(bookingId: Long): Booking {
        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { RuntimeException("Booking not found") }

        booking.status = BookingStatus.CANCELLED

        if (booking.slot.status == Status.BOOKED) {
            booking.slot.status = Status.AVAILABLE
            scheduleSlotRepository.save(booking.slot)
        }

        return bookingRepository.save(booking)
    }
}
