package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.repository.ServiceRepository
import org.springframework.stereotype.Service

@Service
class ScheduleSlotService(
    private val scheduleSlotRepository: ScheduleSlotRepository,
    private val serviceRepository: ServiceRepository,
) {
    fun getAllAvailableSlots(): List<ScheduleSlot> =
        scheduleSlotRepository.findAllByStatus(Status.AVAILABLE)

    fun getAvailableSlotsForProvider(providerId: Long): List<ScheduleSlot> =
        scheduleSlotRepository.findAllByProvider_IdAndStatus(providerId, Status.AVAILABLE)

    fun getAvailableSlotsForService(serviceId: Long): List<ScheduleSlot> {
        val service = serviceRepository.findById(serviceId).orElseThrow {
            RuntimeException("Service not found")
        }
        return scheduleSlotRepository.findAllByProvider_IdAndStatus(service.provider.id!!, Status.AVAILABLE)
    }

    fun markSlotAsBooked(slotId: Long): ScheduleSlot {
        val slot = scheduleSlotRepository.findById(slotId).orElseThrow { RuntimeException("Slot not found") }
        val updated = slot.copy(status = Status.BOOKED)
        return scheduleSlotRepository.save(updated)
    }

    fun createSlot(slot: ScheduleSlot): ScheduleSlot =
        scheduleSlotRepository.save(slot)

    fun deleteSlot(id: Long) =
        scheduleSlotRepository.deleteById(id)
}