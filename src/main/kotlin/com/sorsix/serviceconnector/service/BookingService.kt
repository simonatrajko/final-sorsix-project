package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.DTO.BookingRequestDto
import com.sorsix.serviceconnector.model.Booking
import com.sorsix.serviceconnector.model.ServiceSeeker
import com.sorsix.serviceconnector.model.Services


interface BookingService {
    fun createBooking(
        seeker: ServiceSeeker,
        service: Services,
        slotId: Long,
        isRecurring: Boolean
    ): Booking
    fun respondToBooking(bookingId: Long, accept: Boolean): Booking
    fun getBookingsForProvider(providerId: Long): List<Booking>
    fun getBookingsForSeeker(seekerId: Long): List<Booking>
    fun cancelBooking(bookingId: Long, cancelAllRecurring: Boolean): Booking
    fun completeBooking(bookingId: Long): Booking
    fun getBookingById(bookingId: Long): Booking
    fun createBooking(serviceId: Long, slotId: Long, isRecurring: Boolean, username: String): Booking
}