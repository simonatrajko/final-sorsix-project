package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.Booking
import org.springframework.data.jpa.repository.JpaRepository

interface BookingRepository: JpaRepository<Booking, Long> {
    fun findByProviderId(providerId: Long): List<Booking>
    fun findByClientId(clientId: Long): List<Booking>
}