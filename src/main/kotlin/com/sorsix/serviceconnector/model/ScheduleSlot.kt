package com.sorsix.serviceconnector.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity
data class ScheduleSlot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val start_time: Instant,
    val end_time: Instant,
    val slot_id: Long,//used if we need to colab with other services e. every monday at 10:00 we will have some offer
    var status: Status,
    val created_at: Instant,

    @ManyToOne
    @JoinColumn(name = "provider_id")
    val provider: User
)

enum class Status {
    AVAILABLE,
    BOOKED,
}