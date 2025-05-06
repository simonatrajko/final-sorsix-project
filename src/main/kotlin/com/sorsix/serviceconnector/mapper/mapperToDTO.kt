package com.sorsix.serviceconnector.mapper

import com.sorsix.serviceconnector.DTO.BookingDto
import com.sorsix.serviceconnector.DTO.ProviderDTO
import com.sorsix.serviceconnector.DTO.ServiceDTO
import com.sorsix.serviceconnector.model.Booking
import com.sorsix.serviceconnector.model.Services

fun Services.toDto(): ServiceDTO =
    ServiceDTO(
        id = this.id!!,
        title = this.title,
        description = this.description,
        price = this.price,
        duration = this.duration,
        category = this.category.name,
        provider = ProviderDTO(
            id = this.provider.id!!,
            fullName = this.provider.fullName,
            email = this.provider.email,
            location = this.provider.location
        )
    )



fun Booking.toDto(): BookingDto =
    BookingDto(
        id = this.id ?: -1,
        seekerId = this.client.id ?: -1,
        providerId = this.provider.id ?: -1,
        serviceId = this.service.id ?: -1,
        slotId = this.slot.id ?: -1,
        status = this.status.name,
        recurring = this.isRecurring,
        createdAt = this.createdAt
    )


