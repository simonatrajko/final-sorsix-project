package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.BookingStatus
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class BookingCleanupService(
    private val bookingRepository: BookingRepository,
    private val scheduleSlotRepository: ScheduleSlotRepository
) {

    @Scheduled(fixedRate = 60 * 60 * 1000) // секој 1 час
    fun cancelExpiredPendingBookings() {
        val threshold = Instant.now().minusSeconds(24 * 3600)

        val expiredBookings = bookingRepository
            .findAllByStatusAndCreatedAtBefore(BookingStatus.PENDING, threshold)

        expiredBookings.forEach { booking ->
            booking.status = BookingStatus.CANCELLED
            bookingRepository.save(booking)

            if (booking.slot.status == Status.BOOKED) {
                val updatedSlot = booking.slot.copy(status = Status.AVAILABLE)
                scheduleSlotRepository.save(updatedSlot)
            }
        }

        if (expiredBookings.isNotEmpty()) {
            println("✅ Canceled ${expiredBookings.size} expired bookings.")
        }
    }
}
