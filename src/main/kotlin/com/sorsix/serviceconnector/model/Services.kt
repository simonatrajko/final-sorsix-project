package com.sorsix.serviceconnector.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "service")
data class Services(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var title: String,
    var description: String,
    var price: BigDecimal,
    var duration: Int,
    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: ServiceCategory,//za search

    @CreationTimestamp
    var createdAt: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "provider_id")
    var provider: ServiceProvider
)