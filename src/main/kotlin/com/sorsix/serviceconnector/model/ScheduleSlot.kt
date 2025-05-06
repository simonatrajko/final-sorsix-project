package com.sorsix.serviceconnector.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant
import java.time.LocalTime

@Entity
data class ScheduleSlot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val start_time: LocalTime,
    val end_time: LocalTime,
    val slot_id: Long,//used if we need to colab with other services e. every monday at 10:00 we will have some offer
    @Enumerated(EnumType.STRING)
    val dayOfWeek: DayOfWeek,
    @Enumerated(EnumType.STRING)
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
enum class DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
