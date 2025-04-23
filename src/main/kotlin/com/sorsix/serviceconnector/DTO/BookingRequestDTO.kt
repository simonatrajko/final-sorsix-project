package com.sorsix.serviceconnector.DTO

data class BookingRequestDto(
    val seekerId: Long,
    val serviceId: Long,
    val slotId: Long,
    val isRecurring: Boolean
)
