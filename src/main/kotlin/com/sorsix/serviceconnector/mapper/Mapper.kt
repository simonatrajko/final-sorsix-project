package com.sorsix.serviceconnector.mapper

import com.sorsix.serviceconnector.DTO.BookingDto
import com.sorsix.serviceconnector.DTO.ProviderDTO
import com.sorsix.serviceconnector.DTO.ServiceDTO
import com.sorsix.serviceconnector.model.Booking
import com.sorsix.serviceconnector.model.Services
import org.springframework.stereotype.Component

@Component
class Mapper {

    fun toDto(service: Services): ServiceDTO =
        ServiceDTO(
            id = service.id!!,
            title = service.title,
            description = service.description,
            price = service.price,
            duration = service.duration,
            category = service.category.name,
            provider = ProviderDTO(
                id = service.provider.id!!,
                fullName = service.provider.fullName,
                email = service.provider.email,
                location = service.provider.location
            )
        )

    fun toDto(booking: Booking): BookingDto =
        BookingDto(
            id = booking.id ?: -1,
            seekerId = booking.client.id ?: -1,
            providerId = booking.provider.id ?: -1,
            serviceId = booking.service.id ?: -1,
            slotId = booking.slot.id ?: -1,
            status = booking.status.name,
            recurring = booking.isRecurring,
            createdAt = booking.createdAt
        )
}
