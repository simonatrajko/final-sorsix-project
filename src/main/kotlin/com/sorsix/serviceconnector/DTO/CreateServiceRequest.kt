package com.sorsix.serviceconnector.DTO

import java.math.BigDecimal

data class CreateServiceRequest(
    val title: String,
    val description: String,
    val price: BigDecimal,
    val duration: Int,
    val categoryId: Long
)
