package com.sorsix.serviceconnector.service.impl

import com.sorsix.serviceconnector.model.BookingStatus
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant

@Configuration
class ClockConfig {
    @Bean
    fun clock(): Clock = Clock.systemDefaultZone()
}

@Service
class BookingCleanupServiceImpl(
    private val bookingRepository: BookingRepository,
    private val scheduleSlotRepository: ScheduleSlotRepository,
    private val clock: Clock
) {

    private val logger = LoggerFactory.getLogger(BookingCleanupServiceImpl::class.java)


    @Scheduled(fixedRate = 60 * 60 * 1000)
    fun cancelExpiredPendingBookings() {
        val now = Instant.now(clock)
        val threshold = now.minusSeconds(24 * 3600) // 24 hours ago

        logger.info("Running cleanup for expired bookings before $threshold")

        val expiredBookings = bookingRepository
            .findAllByStatusAndCreatedAtBefore(BookingStatus.PENDING, threshold)

        var cancelledCount = 0

        for (expiredBooking in expiredBookings) {
            try {
                expiredBooking.status = BookingStatus.CANCELLED
                val slot = expiredBooking.slot
                val updatedSlot = slot.copy(status = Status.AVAILABLE)
                scheduleSlotRepository.save(updatedSlot)
                bookingRepository.save(expiredBooking)
                logger.info("Cancelled booking ID ${expiredBooking.id}")
                cancelledCount++
            } catch (e: Exception) {
                logger.error("Failed to cancel booking ID ${expiredBooking.id}: ${e.message}")
            }
        }

        logger.info("Canceled $cancelledCount expired bookings.")
    }
}
