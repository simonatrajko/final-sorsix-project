package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.service.impl.BookingServiceImpl
import com.sorsix.serviceconnector.service.impl.ServiceSeekerServiceImpl
import com.sorsix.serviceconnector.service.impl.ServicesServiceImpl
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.sorsix.serviceconnector.DTO.BookingRequestDto
import com.sorsix.serviceconnector.model.Booking
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingServiceImpl,
    private val servicesService: ServicesServiceImpl,
    private val serviceSeekerService: ServiceSeekerServiceImpl
) {
    @PreAuthorize("hasAuthority('SEEKER')")
    @PostMapping
    fun createBooking(@RequestBody dto: BookingRequestDto): ResponseEntity<Booking> {
        val seeker = serviceSeekerService.findById(dto.seekerId)
        val service = servicesService.getServiceById(dto.serviceId)
        val booking = bookingService.createBooking(seeker, service, dto.slotId, dto.isRecurring)
        return ResponseEntity.ok(booking)
    }

    @PreAuthorize("hasAuthority('PROVIDER')")
    @PutMapping("/{id}/confirm")
    fun confirmBooking(@PathVariable id: Long): ResponseEntity<Booking> {
        val booking = bookingService.respondToBooking(id, accept = true)
        return ResponseEntity.ok(booking)
    }

    @PreAuthorize("hasAuthority('PROVIDER')")
    @PutMapping("/{id}/reject")
    fun rejectBooking(@PathVariable id: Long): ResponseEntity<Booking> {
        val booking = bookingService.respondToBooking(id, accept = false)
        return ResponseEntity.ok(booking)
    }

    @PreAuthorize("hasAuthority('PROVIDER')")
    @PutMapping("/{id}/complete")
    fun completeBooking(@PathVariable id: Long): ResponseEntity<Booking> {
        val booking = bookingService.completeBooking(id)
        return ResponseEntity.ok(booking)
    }

    @PreAuthorize("hasAuthority('SEEKER')")
    @PutMapping("/{id}/cancel")
    fun cancelBooking(
        @PathVariable id: Long,
        @RequestParam(required = false, defaultValue = "false") cancelAllRecurring: Boolean
    ): ResponseEntity<Booking> {
        val booking = bookingService.cancelBooking(id, cancelAllRecurring)
        return ResponseEntity.ok(booking)
    }

    @PreAuthorize("hasAuthority('SEEKER')")
    @GetMapping("/seeker/{id}")
    fun getBookingsForSeeker(@PathVariable id: Long): ResponseEntity<List<Booking>> =
        ResponseEntity.ok(bookingService.getBookingsForSeeker(id))

    @PreAuthorize("hasAuthority('PROVIDER')")
    @GetMapping("/provider/{id}")
    fun getBookingsForProvider(@PathVariable id: Long): ResponseEntity<List<Booking>> =
        ResponseEntity.ok(bookingService.getBookingsForProvider(id))
}
