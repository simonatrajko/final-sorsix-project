package com.sorsix.serviceconnector.model

import java.math.BigDecimal
import java.time.LocalDateTime

//@Entity
data class Service(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var title: String,
    var description: String,
    var price: BigDecimal,
    var duration: Int,
//    @ManyToOne
//    @JoinColumn(name = "category_id")
    var category: ServiceCategory,//za search

//    @CreationTimestamp
    var createdAt: LocalDateTime,

//    @ManyToOne
//    @JoinColumn(name = "provider_id")
    var provider: ServiceProvider
)