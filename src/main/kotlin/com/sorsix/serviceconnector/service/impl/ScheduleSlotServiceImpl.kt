package com.sorsix.serviceconnector.service.impl

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.repository.ServiceRepository
import com.sorsix.serviceconnector.service.ScheduleSlotService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ScheduleSlotServiceImpl(
    private val scheduleSlotRepository: ScheduleSlotRepository,
    private val serviceRepository: ServiceRepository,
): ScheduleSlotService {
    override fun getAllAvailableSlots(): List<ScheduleSlot> =
        scheduleSlotRepository.findAllByStatus(Status.AVAILABLE)

    override fun getAvailableSlotsForProvider(providerId: Long?): List<ScheduleSlot> =
        scheduleSlotRepository.findAllByProvider_IdAndStatus(providerId, Status.AVAILABLE)

    //i dont need this
    override fun getAvailableSlotsForService(serviceId: Long): List<ScheduleSlot> {
        val service = serviceRepository.findById(serviceId).orElseThrow {
            RuntimeException("Service not found")
        }
        return scheduleSlotRepository.findAllByProvider_IdAndStatus(service.provider.id!!, Status.AVAILABLE)
    }

    override fun markSlotAsBooked(slotId: Long): ScheduleSlot {
        val slot = scheduleSlotRepository.findById(slotId).orElseThrow { RuntimeException("Slot not found") }
        val updated = slot.copy(status = Status.BOOKED)
        return scheduleSlotRepository.save(updated)
    }

    override fun createSlot(slot: ScheduleSlot): ScheduleSlot =
        scheduleSlotRepository.save(slot)

    override fun deleteSlot(id: Long) =
        scheduleSlotRepository.deleteById(id)

    override fun getSlotById(slotId: Long): ScheduleSlot =
        scheduleSlotRepository.findById(slotId)
            .orElseThrow { NoSuchElementException("Slot not found") }

    override fun getAllSlotsForProvider(providerId: Long): List<ScheduleSlot> =
        scheduleSlotRepository.findAllByProvider_Id(providerId)

}