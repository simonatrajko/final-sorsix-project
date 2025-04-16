package com.sorsix.serviceconnector.model

import java.time.Instant

//@Entity
data class Booking(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var createdAt: Instant,

//    @ManyToOne
//    @JoinColumn(name = "client_id")
    var client: ServiceSeeker,

//    @ManyToOne
//    @JoinColumn(name = "provider_id")
    var provider: ServiceProvider,

//    @ManyToOne
//    @JoinColumn(name = "service_id")
    var service: Service,

//    @ManyToOne
//    @JoinColumn(name = "slot_id")
    var slot: ScheduleSlot,
//    @Enumerated(EnumType.STRING)
    var status: BookingStatus = BookingStatus.PENDING,//this is the default value
)

enum class BookingStatus {
    PENDING,       // Booking created, waiting for provider
    CONFIRMED,     // Provider accepted
    REJECTED,      // Provider rejected
    CANCELLED,     // Cancelled by seeker or provider
    COMPLETED      // Service was delivered
}
