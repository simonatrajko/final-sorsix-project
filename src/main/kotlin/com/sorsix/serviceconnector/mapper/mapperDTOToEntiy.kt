package com.sorsix.serviceconnector.mapper

import com.sorsix.serviceconnector.DTO.CreateServiceRequest
import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.model.Services
import java.time.LocalDateTime

fun CreateServiceRequest.toEntity(
    category: ServiceCategory,
    provider: ServiceProvider
): Services =
    Services(
        title = this.title,
        description = this.description,
        price = this.price,
        duration = this.duration,
        category = category,
        provider = provider,
        createdAt = LocalDateTime.now()
    )
