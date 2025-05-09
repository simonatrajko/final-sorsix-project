package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.DTO.CreateSlotRequest
import com.sorsix.serviceconnector.model.DayOfWeek
import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.security.AppUserDetails
import com.sorsix.serviceconnector.service.ScheduleSlotService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    private val scheduleSlotService: ScheduleSlotService, ) {

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
            startTime = request.startTime,
            endTime = request.endTime,
            status = Status.AVAILABLE,
            created_at = Instant.now(),
            provider = provider,
            dayOfWeek = request.dayOfWeek,
        )

        val created = scheduleSlotService.createSlot(slot)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
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

    @GetMapping("/services/{serviceId}/available-slots/day")
    @PreAuthorize("hasRole('SEEKER')")
    fun getAvailableSlotsByDayOfWeek(
        @PathVariable serviceId: Long,
        @RequestParam dayOfWeek: String,
        pageable: Pageable
    ): ResponseEntity<Page<ScheduleSlot>> {
        val day = try {
            DayOfWeek.valueOf(dayOfWeek.uppercase())
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid day of week")
        }

        val page = scheduleSlotService.getAvailableSlotsForServiceAndDay(serviceId, day, pageable)
        return ResponseEntity.ok(page)
    }

}

