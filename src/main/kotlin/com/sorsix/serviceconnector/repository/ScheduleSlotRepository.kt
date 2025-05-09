package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.DayOfWeek
import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.Status
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalTime

interface ScheduleSlotRepository: JpaRepository<ScheduleSlot, Long> {
    fun findAllByProvider_IdAndStatus(providerId: Long?, status: Status): List<ScheduleSlot>
    fun findAllByStatus(status: Status): List<ScheduleSlot>
    fun findAllByProvider_Id(providerId: Long): List<ScheduleSlot>
    fun findByProviderIdAndStatusAndDayOfWeek(
        providerId: Long,
        status: Status,
        dayOfWeek: DayOfWeek,
        pageable: Pageable
    ): Page<ScheduleSlot>
    fun findByProviderIdAndStartTimeAndEndTime
    (
        providerId: Long,
        startTime: LocalTime,
        endTime: LocalTime
    ): ScheduleSlot?


}