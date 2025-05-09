package com.sorsix.serviceconnector.model

import jakarta.persistence.Column
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

    @Column(name = "start_time")
    val startTime: LocalTime,

    @Column(name = "end_time")
    val endTime: LocalTime,
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
