package com.sorsix.serviceconnector.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.sorsix.serviceconnector.DTO.BookingRequestDto
import com.sorsix.serviceconnector.model.*
import com.sorsix.serviceconnector.service.BookingService
import com.sorsix.serviceconnector.service.ServiceSeekerService
import com.sorsix.serviceconnector.service.ServicesService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@WebMvcTest(BookingController::class)
class BookingControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var bookingService: BookingService

    @MockBean
    lateinit var servicesService: ServicesService

    @MockBean
    lateinit var serviceSeekerService: ServiceSeekerService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `createBooking should return booking`() {
        // Arrange
        val seeker = ServiceSeeker("seeker", "pw", "email", "Seeker", "", "Loc").apply { id = 1L }
        val provider = ServiceProvider("prov", "pw", "email", "Prov", "", "Loc", 5).apply { id = 2L }

        val service = Services(
            1L,
            "Clean",
            "desc",
            BigDecimal.TEN,
            60,
            ServiceCategory(1L, "Home"),
            LocalDateTime.now(),
            provider
        )

        val slot = ScheduleSlot(
            1L,
            Instant.now(),
            Instant.now().plusSeconds(3600),
            1L,
            Status.AVAILABLE,
            Instant.now(),
            provider
        )

        val booking = Booking(
            1L,
            Instant.now(),
            seeker,
            provider,
            service,
            slot,
            BookingStatus.PENDING,
            isRecurring = false
        )

        val dto = BookingRequestDto(
            seekerId = 1L,
            serviceId = 1L,
            slotId = 1L,
            isRecurring = false
        )

        `when`(serviceSeekerService.findById(1L)).thenReturn(seeker)
        `when`(servicesService.getServiceById(1L)).thenReturn(service)
        `when`(bookingService.createBooking(seeker, service, 1L, false)).thenReturn(booking)

        // Act & Assert
        mockMvc.post("/api/bookings") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(dto)
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(1L) }
                jsonPath("$.status") { value("PENDING") }
            }
    }
    @Test
    fun `confirmBooking should return OK with confirmed booking`() {
        val booking = Booking(
            id = 1L,
            createdAt = Instant.now(),
            client = mock(ServiceSeeker::class.java),
            provider = mock(ServiceProvider::class.java),
            service = mock(Services::class.java),
            slot = mock(ScheduleSlot::class.java),
            status = BookingStatus.CONFIRMED,
            isRecurring = false
        )
        `when`(bookingService.respondToBooking(1L, true)).thenReturn(booking)

        mockMvc.perform(put("/api/bookings/1/confirm"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("CONFIRMED"))
    }

    @Test
    fun `rejectBooking should return OK with rejected booking`() {
        val booking = Booking(
            id = 1L,
            createdAt = Instant.now(),
            client = mock(ServiceSeeker::class.java),
            provider = mock(ServiceProvider::class.java),
            service = mock(Services::class.java),
            slot = mock(ScheduleSlot::class.java),
            status = BookingStatus.REJECTED,
            isRecurring = false
        )
        `when`(bookingService.respondToBooking(1L, false)).thenReturn(booking)

        mockMvc.perform(put("/api/bookings/1/reject"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("REJECTED"))
    }

    @Test
    fun `completeBooking should return OK with completed booking`() {
        val booking = Booking(
            id = 1L,
            createdAt = Instant.now(),
            client = mock(ServiceSeeker::class.java),
            provider = mock(ServiceProvider::class.java),
            service = mock(Services::class.java),
            slot = mock(ScheduleSlot::class.java),
            status = BookingStatus.COMPLETED,
            isRecurring = false
        )
        `when`(bookingService.completeBooking(1L)).thenReturn(booking)

        mockMvc.perform(put("/api/bookings/1/complete"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("COMPLETED"))
    }

    @Test
    fun `cancelBooking should return OK with cancelled booking`() {
        val booking = Booking(
            id = 1L,
            createdAt = Instant.now(),
            client = mock(ServiceSeeker::class.java),
            provider = mock(ServiceProvider::class.java),
            service = mock(Services::class.java),
            slot = mock(ScheduleSlot::class.java),
            status = BookingStatus.CANCELLED,
            isRecurring = false
        )
        `when`(bookingService.cancelBooking(1L, false)).thenReturn(booking)

        mockMvc.perform(put("/api/bookings/1/cancel"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("CANCELLED"))
    }

}
