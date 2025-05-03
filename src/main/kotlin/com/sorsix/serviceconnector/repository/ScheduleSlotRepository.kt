package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.Status
import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleSlotRepository: JpaRepository<ScheduleSlot, Long> {
//    fun findAllByProvider_Id(providerId: Long): List<ScheduleSlot>
    fun findAllByProvider_IdAndStatus(providerId: Long?, status: Status): List<ScheduleSlot>
    fun findAllByStatus(status: Status): List<ScheduleSlot>
    fun findAllByProvider_Id(providerId: Long): List<ScheduleSlot>
}