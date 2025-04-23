package com.sorsix.serviceconnector.DTO

import java.math.BigDecimal

data class ServiceDTO(
    val id: Long,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val duration: Int,
    val category: String,
    val provider: ProviderDTO
)