package com.sorsix.serviceconnector

import com.sorsix.serviceconnector.DTO.ProviderDTO
import com.sorsix.serviceconnector.DTO.ServiceDTO
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
