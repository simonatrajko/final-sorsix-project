package com.sorsix.serviceconnector.service

interface BookingCleanupService {
    fun cancelExpiredPendingBookings()
}