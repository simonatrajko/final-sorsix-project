package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.DTO.BookingDto

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.sorsix.serviceconnector.DTO.BookingRequestDto
import com.sorsix.serviceconnector.DTO.ServiceDTO
import com.sorsix.serviceconnector.mapper.Mapper
import com.sorsix.serviceconnector.model.Booking
import com.sorsix.serviceconnector.service.BookingService
import com.sorsix.serviceconnector.service.ScheduleSlotService
import com.sorsix.serviceconnector.service.ServiceProviderService
import com.sorsix.serviceconnector.service.ServiceSeekerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/bookings")
class BookingController {
    private val bookingService: BookingService
    private val scheduleSlotService: ScheduleSlotService
    private val serviceSeekerService: ServiceSeekerService
    private val serviceProviderService: ServiceProviderService
    private val bookingMapper: Mapper

    constructor(
        bookingService: BookingService,
        scheduleSlotService: ScheduleSlotService,
        serviceSeekerService: ServiceSeekerService,
        serviceProviderService: ServiceProviderService,
        bookingMapper: Mapper
    ) {
        this.bookingService = bookingService
        this.scheduleSlotService = scheduleSlotService
        this.serviceSeekerService = serviceSeekerService
        this.serviceProviderService = serviceProviderService
        this.bookingMapper = bookingMapper
    }

    @PostMapping("/{serviceId}")
    @PreAuthorize("hasRole('SEEKER')")
    fun createBooking(
        @PathVariable serviceId: Long,
        @RequestBody dto: BookingRequestDto,
        @AuthenticationPrincipal user: UserDetails?
    ): ResponseEntity<BookingDto> {
        user ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated")

        val booking = bookingService.handleCreateBookingRequest(serviceId, dto.slotId, dto.isRecurring, user.username)
        return ResponseEntity.ok(bookingMapper.toDto(booking))
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping("/{id}/confirm")
    fun confirmBooking(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: UserDetails?
    ): ResponseEntity<Booking> {
        val provider = user?.username?.let { serviceProviderService.findByUsername(it) }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Provider not found")

        val booking = bookingService.getBookingById(id)

        if (booking.service.provider.id != provider.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot confirm this booking")
        }

        val updated = bookingService.respondToBooking(id, accept = true)
        return ResponseEntity.ok(updated)
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping("/{id}/reject")
    fun rejectBooking(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: UserDetails?
    ): ResponseEntity<Booking> {
        val provider = user?.username?.let { serviceProviderService.findByUsername(it) }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Provider not found")

        val booking = bookingService.getBookingById(id)

        if (booking.service.provider.id != provider.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot reject this booking")
        }

        val updated = bookingService.respondToBooking(id, accept = false)
        return ResponseEntity.ok(updated)
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping("/{id}/complete")
    fun completeBooking(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: UserDetails?
    ): ResponseEntity<Booking> {
        val provider = user?.username?.let { serviceProviderService.findByUsername(it) }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Provider not found")

        val booking = bookingService.getBookingById(id)

        if (booking.service.provider.id != provider.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot complete this booking")
        }

        val updated = bookingService.completeBooking(id)
        return ResponseEntity.ok(updated)
    }

    @PreAuthorize("hasRole('SEEKER')")
    @PutMapping("/{id}/cancel")
    fun cancelBooking(
        @PathVariable id: Long,
        @RequestParam(required = false, defaultValue = "false") cancelAllRecurring: Boolean,
        @AuthenticationPrincipal user: UserDetails?
    ): ResponseEntity<Booking> {
        val seeker = user?.username?.let { serviceSeekerService.findByUsername(it) }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Seeker not found")

        val booking = bookingService.getBookingById(id)

        if (booking.client.id != seeker.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You can only cancel your own bookings")
        }

        val updated = bookingService.cancelBooking(id, cancelAllRecurring)
        return ResponseEntity.ok(updated)
    }

    @PreAuthorize("hasRole('SEEKER')")
    @GetMapping("/my-services-seeker")
    fun getBookedServicesForSeeker(@AuthenticationPrincipal user: UserDetails): ResponseEntity<List<ServiceDTO>> {
        val seeker = serviceSeekerService.findByUsername(user.username)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        val bookings = bookingService.getBookingsForSeeker(seeker.id!!)
        val services = bookings.map { it.service }.distinct().map { bookingMapper.toDto(it) }
        return ResponseEntity.ok(services)
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/provider/pending")
    fun getPendingBookingsForProvider(
        @AuthenticationPrincipal user: UserDetails?,
        pageable: Pageable
    ): ResponseEntity<Page<BookingDto>> {
        val provider = user?.username?.let { serviceProviderService.findByUsername(it) }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Provider not found")

        val bookingsPage = bookingService.getPendingBookingsForProvider(provider.id!!,pageable)
        val dtoPage = bookingsPage.map { bookingMapper.toDto(it) }

        return ResponseEntity.ok(dtoPage)
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/provider/confirmed")
    fun getConfirmedBookingsForProvider(
        @AuthenticationPrincipal user: UserDetails?,
        pageable: Pageable
    ): ResponseEntity<Page<BookingDto>> {
        val provider = user?.username?.let { serviceProviderService.findByUsername(it) }
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Provider not found")

        val bookingsPage = bookingService.getConfirmedBookingsForProvider(provider.id!!, pageable)
        val dtoPage = bookingsPage.map { bookingMapper.toDto(it) }

        return ResponseEntity.ok(dtoPage)
    }

}
