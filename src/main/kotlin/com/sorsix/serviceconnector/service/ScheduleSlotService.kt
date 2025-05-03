package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.Status

interface ScheduleSlotService {
    fun getAllAvailableSlots(): List<ScheduleSlot>
    fun getAvailableSlotsForProvider(providerId: Long?): List<ScheduleSlot>
    fun getAvailableSlotsForService(serviceId: Long): List<ScheduleSlot>
    fun markSlotAsBooked(slotId: Long): ScheduleSlot
    fun createSlot(slot: ScheduleSlot): ScheduleSlot
    fun deleteSlot(id: Long)
    fun getSlotById(id: Long): ScheduleSlot
    fun getAllSlotsForProvider(providerId: Long): List<ScheduleSlot>

}