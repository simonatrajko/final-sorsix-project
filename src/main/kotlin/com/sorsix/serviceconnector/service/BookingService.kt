package com.sorsix.serviceconnector.service


import com.sorsix.serviceconnector.model.Booking
import com.sorsix.serviceconnector.model.ServiceSeeker
import com.sorsix.serviceconnector.model.Services
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Page



interface BookingService {
    fun createBooking(
        seeker: ServiceSeeker,
        service: Services,
        slotId: Long,
        isRecurring: Boolean
    ): Booking
    fun handleCreateBookingRequest(serviceId: Long, slotId: Long, isRecurring: Boolean, username: String): Booking
    fun respondToBooking(bookingId: Long, accept: Boolean): Booking
    fun getBookingsForSeeker(seekerId: Long): List<Booking>
    fun cancelBooking(bookingId: Long, cancelAllRecurring: Boolean): Booking
    fun completeBooking(bookingId: Long): Booking
    fun getBookingById(bookingId: Long): Booking
    fun getPendingBookingsForProvider(providerId: Long, pageable: Pageable): Page<Booking>
    fun getConfirmedBookingsForProvider(providerId: Long, pageable: Pageable): Page<Booking>
}