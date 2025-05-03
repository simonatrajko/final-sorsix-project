package com.sorsix.serviceconnector.DTO

import java.math.BigDecimal
import jakarta.validation.constraints.*

data class CreateServiceRequest(
    @field:NotBlank(message = "Title must not be blank")
    val title: String,

    @field:NotBlank(message = "Description must not be blank")
    val description: String,

    @field:DecimalMin("0.0", inclusive = false)
    val price: BigDecimal,

    @field:Min(1)
    val duration: Int,

    val categoryId: Long
)
