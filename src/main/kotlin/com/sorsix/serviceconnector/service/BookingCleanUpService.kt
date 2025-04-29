package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.BookingStatus
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant

//@Service
//class BookingCleanupService(
//    private val bookingRepository: BookingRepository,
//    private val scheduleSlotRepository: ScheduleSlotRepository
//) {
//    private val logger = LoggerFactory.getLogger(BookingCleanupService::class.java)
//
//    @Scheduled(fixedRate = 60 * 60 * 1000) // Runs every hour
//    @Transactional
//    fun cancelExpiredPendingBookings() {
//        val threshold = Instant.now().minusSeconds(24 * 3600)
//        logger.info("Running cleanup for expired bookings before $threshold")
//
//        val expiredBookings = try {
//            bookingRepository.findAllByStatusAndCreatedAtBefore(BookingStatus.PENDING, threshold)
//        } catch (ex: Exception) {
//            logger.error("Failed to fetch expired bookings", ex)
//            return
//        }
//
//        if (expiredBookings.isEmpty()) {
//            logger.info("No expired bookings found.")
//            return
//        }
//
//        expiredBookings.forEach { booking ->
//            try {
//                booking.status = BookingStatus.CANCELLED
//                val savedBooking = bookingRepository.save(booking)
//                logger.info("Cancelled booking ID ${savedBooking.id}")
//
//                if (booking.slot.status == Status.BOOKED) {
//                    val updatedSlot = booking.slot.copy(status = Status.AVAILABLE)
//                    val savedSlot = scheduleSlotRepository.save(updatedSlot)
//                    logger.info("Freed up slot ID ${savedSlot.id}")
//                }
//            } catch (ex: Exception) {
//                logger.error("Failed to process booking ID ${booking.id}", ex)
//            }
//        }
//
//        logger.info("Canceled ${expiredBookings.size} expired bookings.")
//    }
//}
@Configuration
class ClockConfig {
    @Bean
    fun clock(): Clock = Clock.systemDefaultZone()
}

@Service
class BookingCleanupService(
    private val bookingRepository: BookingRepository,
    private val scheduleSlotRepository: ScheduleSlotRepository,
    private val clock: Clock
) {
    private val logger = LoggerFactory.getLogger(BookingCleanupService::class.java)

    @Scheduled(fixedRate = 60 * 60 * 1000) // Runs every hour
    @Transactional
    fun cancelExpiredPendingBookings() {
        val threshold = Instant.now(clock).minusSeconds(24 * 3600)
        logger.info("Running cleanup for expired bookings before $threshold")

        val expiredBookings = try {
            bookingRepository.findAllByStatusAndCreatedAtBefore(BookingStatus.PENDING, threshold)
        } catch (ex: Exception) {
            logger.error("Failed to fetch expired bookings", ex)
            return
        }

        if (expiredBookings.isEmpty()) {
            logger.info("No expired bookings found.")
            return
        }

        expiredBookings.forEach { booking ->
            try {
                booking.status = BookingStatus.CANCELLED
                val savedBooking = bookingRepository.save(booking)
                logger.info("Cancelled booking ID ${savedBooking.id}")

                if (booking.slot.status == Status.BOOKED) {
                    val updatedSlot = booking.slot.copy(status = Status.AVAILABLE)
                    val savedSlot = scheduleSlotRepository.save(updatedSlot)
                    logger.info("Freed up slot ID ${savedSlot.id}")
                }
            } catch (ex: Exception) {
                logger.error("Failed to process booking ID ${booking.id}", ex)
            }
        }

        logger.info("Canceled ${expiredBookings.size} expired bookings.")
    }
}