package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.Booking
import com.sorsix.serviceconnector.model.BookingStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface BookingRepository : JpaRepository<Booking, Long> {
    fun findByProviderId(providerId: Long): List<Booking>
    fun findByClientId(clientId: Long): List<Booking>
    fun findBySlotId(slotId: Long): List<Booking>
    fun findAllByStatusAndCreatedAtBefore(
        status: BookingStatus,
        time: Instant
    ): List<Booking>

}