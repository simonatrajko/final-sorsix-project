import com.sorsix.serviceconnector.exeptions.*
import com.sorsix.serviceconnector.model.*
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.service.impl.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class BookingServiceImplTest {

    private val bookingRepository = mock(BookingRepository::class.java)
    private val slotRepository = mock(ScheduleSlotRepository::class.java)
    private val seekerService = mock(ServiceSeekerServiceImpl::class.java)
    private val servicesService = mock(ServicesServiceImpl::class.java)
    private val slotService = mock(ScheduleSlotServiceImpl::class.java)

    private val bookingService = BookingServiceImpl(
        bookingRepository,
        slotRepository,
        seekerService,
        slotService,
        servicesService
    )

    private val seeker = ServiceSeeker("seeker", "hashed", "mail", "Seeker Name", "", "Skopje").apply { id = 1L }
    private val provider = ServiceProvider("provider", "hashed", "mail", "Provider Name", "", "Skopje").apply { id = 2L }

    private val service = Services(
        id = 10L,
        title = "Haircut",
        description = "Basic",
        price = BigDecimal(30),
        duration = 30,
        category = ServiceCategory(1, "Beauty"),
        provider = provider,
        createdAt = LocalDateTime.now()
    )

    private val slot = ScheduleSlot(
        id = 100L,
        start_time = LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC),
        end_time = LocalDateTime.now().plusDays(1).plusHours(1).toInstant(ZoneOffset.UTC),
        slot_id = 100L,
        status = Status.AVAILABLE,
        created_at = Instant.now(),
        provider = provider
    )

    @Test
    fun `should fail if slot does not belong to service provider`() {
        val username = "seeker"
        val otherProvider = provider.copy(id = 99L)
        val mismatchedSlot = slot.copy(provider = otherProvider)

        `when`(seekerService.findByUsername(username)).thenReturn(seeker)
        `when`(servicesService.getServiceById(service.id!!)).thenReturn(service)
        `when`(slotService.getSlotById(slot.id!!)).thenReturn(mismatchedSlot)

        val ex = assertThrows<ResponseStatusException> {
            bookingService.handleCreateBookingRequest(service.id!!, slot.id!!, false, username)
        }

        assertEquals(HttpStatus.BAD_REQUEST, ex.statusCode)
        assertEquals("Slot does not belong to selected service provider", ex.reason)
    }

    @Test
    fun `should fail if slot already has confirmed booking`() {
        val username = "seeker"
        val confirmedBooking = Booking(1L, Instant.now(), seeker, provider, service, slot, BookingStatus.CONFIRMED, false)

        `when`(seekerService.findByUsername(username)).thenReturn(seeker)
        `when`(servicesService.getServiceById(service.id!!)).thenReturn(service)
        `when`(slotService.getSlotById(slot.id!!)).thenReturn(slot)
        `when`(slotRepository.findById(slot.id!!)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(seeker.id)).thenReturn(emptyList())
        `when`(bookingRepository.findBySlotId(slot.id!!)).thenReturn(listOf(confirmedBooking))

        val ex = assertThrows<SlotAlreadyConfirmedException> {
            bookingService.handleCreateBookingRequest(service.id!!, slot.id!!, false, username)
        }

        assertEquals("Slot with ID ${slot.id} is already confirmed for another booking.", ex.message)
    }

    @Test
    fun `should fail if client has already booked same slot`() {
        val username = "seeker"
        val existingBooking = Booking(99L, Instant.now(), seeker, provider, service, slot, BookingStatus.PENDING, false)

        `when`(seekerService.findByUsername(username)).thenReturn(seeker)
        `when`(servicesService.getServiceById(service.id!!)).thenReturn(service)
        `when`(slotService.getSlotById(slot.id!!)).thenReturn(slot)
        `when`(slotRepository.findById(slot.id!!)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(seeker.id)).thenReturn(listOf(existingBooking))

        val ex = assertThrows<DuplicateBookingException> {
            bookingService.handleCreateBookingRequest(service.id!!, slot.id!!, false, username)
        }

        assertEquals("You already have a booking for this time slot.", ex.message)
    }

    @Test
    fun `should create booking successfully`() {
        val isRecurring = true
        val username = "seeker"

        `when`(seekerService.findByUsername(username)).thenReturn(seeker)
        `when`(servicesService.getServiceById(service.id!!)).thenReturn(service)
        `when`(slotService.getSlotById(slot.id!!)).thenReturn(slot)
        `when`(slotRepository.findById(slot.id!!)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(seeker.id)).thenReturn(emptyList())
        `when`(bookingRepository.findBySlotId(slot.id!!)).thenReturn(emptyList())

        val saved = Booking(200L, Instant.now(), seeker, provider, service, slot.copy(status = Status.BOOKED), BookingStatus.PENDING, isRecurring)
        `when`(bookingRepository.save(any())).thenReturn(saved)

        val result = bookingService.handleCreateBookingRequest(service.id!!, slot.id!!, isRecurring, username)

        assertEquals(seeker, result.client)
        assertEquals(service, result.service)
        assertEquals(isRecurring, result.isRecurring)
        assertEquals(Status.BOOKED, result.slot.status)
        assertEquals(BookingStatus.PENDING, result.status)
    }

    @Test
    fun `should fail if slot is not available`() {
        val username = "seeker"
        val bookedSlot = slot.copy(status = Status.BOOKED)

        `when`(seekerService.findByUsername(username)).thenReturn(seeker)
        `when`(servicesService.getServiceById(service.id!!)).thenReturn(service)
        `when`(slotService.getSlotById(bookedSlot.id!!)).thenReturn(bookedSlot)
        `when`(slotRepository.findById(bookedSlot.id!!)).thenReturn(Optional.of(bookedSlot))

        val ex = assertThrows<NotAvailableSlotException> {
            bookingService.handleCreateBookingRequest(service.id!!, bookedSlot.id!!, false, username)
        }

        assertEquals("Slot is not available", ex.message)
    }
}
