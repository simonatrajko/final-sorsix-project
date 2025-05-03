package com.sorsix.serviceconnector.DTO

import java.time.Instant
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull

data class CreateSlotRequest(
    @field:NotNull(message = "Start time is required")
    @field:FutureOrPresent(message = "Start time must be in the present or future")
    val startTime: Instant,

    @field:NotNull(message = "End time is required")
    @field:FutureOrPresent(message = "End time must be in the present or future")
    val endTime: Instant,

    val slotId: Long? = null // Optional with null default
)