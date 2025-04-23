package com.sorsix.serviceconnector.api

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.service.ScheduleSlotService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/schedules")
class ScheduleSlotController (private val scheduleSlotService: ScheduleSlotService) {
    @GetMapping("/service/{serviceId}/slots")
    fun getAvailableSlots(@PathVariable serviceId: Long): ResponseEntity<List<ScheduleSlot>> {
        val slots = scheduleSlotService.getAvailableSlotsForService(serviceId)
        return ResponseEntity.ok(slots)
    }
}