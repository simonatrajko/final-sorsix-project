package com.sorsix.serviceconnector.DTO

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class UpdatePriceRequest(
    @field:NotNull(message = "Price must not be null")
    @field:Positive(message = "Price must be greater than 0")
    val price: BigDecimal?
)
