package com.sorsix.serviceconnector.DTO

import java.time.Instant


data class BookingDto(
    val id: Long,
    val seekerId: Long,
    val providerId: Long,
    val serviceId: Long,
    val slotId: Long,
    val status: String,
    val recurring: Boolean,
    val createdAt: Instant
)
