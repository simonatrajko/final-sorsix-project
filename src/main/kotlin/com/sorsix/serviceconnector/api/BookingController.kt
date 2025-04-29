package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.service.BookingService
import com.sorsix.serviceconnector.service.ServiceSeekerService
import com.sorsix.serviceconnector.service.ServicesService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.sorsix.serviceconnector.DTO.BookingRequestDto
import com.sorsix.serviceconnector.model.Booking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService,
    private val servicesService: ServicesService,
    private val serviceSeekerService: ServiceSeekerService
) {

    @PostMapping
    fun createBooking(@RequestBody dto: BookingRequestDto): ResponseEntity<Booking> {
        val seeker = serviceSeekerService.findById(dto.seekerId)
        val service = servicesService.getServiceById(dto.serviceId)
        val booking = bookingService.createBooking(seeker, service, dto.slotId, dto.isRecurring)
        return ResponseEntity.ok(booking)
    }

    @PutMapping("/{id}/confirm")
    fun confirmBooking(@PathVariable id: Long): ResponseEntity<Booking> {
        val booking = bookingService.respondToBooking(id, accept = true)
        return ResponseEntity.ok(booking)
    }

    @PutMapping("/{id}/reject")
    fun rejectBooking(@PathVariable id: Long): ResponseEntity<Booking> {
        val booking = bookingService.respondToBooking(id, accept = false)
        return ResponseEntity.ok(booking)
    }

    @PutMapping("/{id}/complete")
    fun completeBooking(@PathVariable id: Long): ResponseEntity<Booking> {
        val booking = bookingService.completeBooking(id)
        return ResponseEntity.ok(booking)
    }

    @PutMapping("/{id}/cancel")
    fun cancelBooking(
        @PathVariable id: Long,
        @RequestParam(required = false, defaultValue = "false") cancelAllRecurring: Boolean
    ): ResponseEntity<Booking> {
        val booking = bookingService.cancelBooking(id, cancelAllRecurring)
        return ResponseEntity.ok(booking)
    }

    @GetMapping("/seeker/{id}")
    fun getBookingsForSeeker(@PathVariable id: Long): ResponseEntity<List<Booking>> =
        ResponseEntity.ok(bookingService.getBookingsForSeeker(id))

    @GetMapping("/provider/{id}")
    fun getBookingsForProvider(@PathVariable id: Long): ResponseEntity<List<Booking>> =
        ResponseEntity.ok(bookingService.getBookingsForProvider(id))
}
