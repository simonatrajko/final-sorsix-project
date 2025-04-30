package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.service.ScheduleSlotService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/schedules")
class ScheduleSlotController(
    private val scheduleSlotService: ScheduleSlotService
) {

    @GetMapping("/provider/{providerId}/slots/available")
    fun getAvailableSlotsForProvider(@PathVariable providerId: Long): List<ScheduleSlot> =
        scheduleSlotService.getAvailableSlotsForProvider(providerId)


    @PostMapping("/slots")
    @PreAuthorize("hasAuthority('PROVIDER')")
    fun createSlot(@RequestBody slot: ScheduleSlot): ResponseEntity<ScheduleSlot> {
        val created = scheduleSlotService.createSlot(slot)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/slots/{slotId}/book")
    fun markSlotAsBooked(@PathVariable slotId: Long): ResponseEntity<ScheduleSlot> {
        val updated = scheduleSlotService.markSlotAsBooked(slotId)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/slots/{slotId}")
    @PreAuthorize("hasAuthority('PROVIDER')")
    fun deleteSlot(@PathVariable slotId: Long): ResponseEntity<Void> {
        scheduleSlotService.deleteSlot(slotId)
        return ResponseEntity.noContent().build()
    }
}
