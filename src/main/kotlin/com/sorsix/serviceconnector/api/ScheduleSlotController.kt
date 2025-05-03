package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.DTO.CreateSlotRequest
import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.ServiceRepository
import com.sorsix.serviceconnector.security.AppUserDetails
import com.sorsix.serviceconnector.service.ScheduleSlotService
import com.sorsix.serviceconnector.service.ServicesService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Validated
@RestController
@RequestMapping("/api/schedules")
class ScheduleSlotController(
    private val scheduleSlotService: ScheduleSlotService,
    private val serviceRepository: ServiceRepository) {

    @GetMapping("/provider/slots")
    @PreAuthorize("hasRole('PROVIDER')")
    fun getAllSlotsForAuthenticatedProvider(): ResponseEntity<List<ScheduleSlot>> {
        val auth = SecurityContextHolder.getContext().authentication
        val userDetails = auth.principal as AppUserDetails
        val provider = userDetails.getUser() as? ServiceProvider
            ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).build()

        val slots = scheduleSlotService.getAllSlotsForProvider(provider.id!!)
        return ResponseEntity.ok(slots)
    }


    @PostMapping("/slots")
    @PreAuthorize("hasRole('PROVIDER')")
    fun createSlot(@Valid @RequestBody request: CreateSlotRequest,
                    bindingResult: BindingResult): ResponseEntity<Any> {

        if (bindingResult.hasErrors()) {
            val errors = bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
            return ResponseEntity.badRequest().body(errors)
        }

        if (request.endTime.isBefore(request.startTime)) {
            return ResponseEntity.badRequest()
                .body(mapOf("error" to "End time must be after start time"))
        }

        val durationHours = java.time.Duration.between(request.startTime, request.endTime).toHours()
        if (durationHours > 24) {
            return ResponseEntity.badRequest()
                .body(mapOf("error" to "Slot duration cannot exceed 24 hours"))
        }

        val auth = SecurityContextHolder.getContext().authentication
        val userDetails = auth.principal as AppUserDetails
        val provider = userDetails.getUser() as? ServiceProvider
            ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).build()

        val slot = ScheduleSlot(
            start_time = request.startTime,
            end_time = request.endTime,
            slot_id = request.slotId ?: System.currentTimeMillis(),
            status = Status.AVAILABLE,
            created_at = Instant.now(),
            provider = provider
        )

        val created = scheduleSlotService.createSlot(slot)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/slots/{slotId}/book")
    @PreAuthorize("hasRole('SEEKER')")
    fun markSlotAsBooked(@PathVariable slotId: Long): ResponseEntity<ScheduleSlot> {
        try {
            val updated = scheduleSlotService.markSlotAsBooked(slotId)
            return ResponseEntity.ok(updated)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null)
        }
    }

    @DeleteMapping("/slots/{slotId}")
    @PreAuthorize("hasRole('PROVIDER')")
    fun deleteSlot(@PathVariable slotId: Long): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication
        val userDetails = auth.principal as AppUserDetails
        val provider = userDetails.getUser() as? ServiceProvider
            ?: return ResponseEntity.status(HttpStatus.FORBIDDEN).build()


        try {

            val slot = scheduleSlotService.getSlotById(slotId)

            if (slot.provider.id != provider.id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(mapOf("error" to "You can only delete your own slots"))
            }

            scheduleSlotService.deleteSlot(slotId)
            return ResponseEntity.noContent().build()
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "Slot not found"))
        }
    }
    @GetMapping("/services/{serviceId}/available-slots")
    @PreAuthorize("hasRole('SEEKER')")
    fun getAvailableSlotsForService(@PathVariable serviceId: Long): ResponseEntity<List<ScheduleSlot>> {
        val service = serviceRepository.findById(serviceId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found") }

        val providerId = service.provider.id
        val availableSlots = scheduleSlotService.getAvailableSlotsForProvider(providerId)
        return ResponseEntity.ok(availableSlots)
    }

}
