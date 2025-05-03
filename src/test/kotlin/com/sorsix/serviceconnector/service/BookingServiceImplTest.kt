import com.sorsix.serviceconnector.exeptions.DuplicateBookingException
import com.sorsix.serviceconnector.exeptions.NotAvailableSlotException
import com.sorsix.serviceconnector.exeptions.NotPendingBookingException
import com.sorsix.serviceconnector.exeptions.ProviderAlreadyBookedException
import com.sorsix.serviceconnector.model.*
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.service.impl.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
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

    private val bookingService = spy(
        BookingServiceImpl(
            bookingRepository,
            slotRepository,
            seekerService,
            slotService,
            servicesService
        )
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
    fun `createBooking should create booking when all is valid`() {
        val isRecurring = true
        val username = "seeker"

        `when`(seekerService.findByUsername(username)).thenReturn(seeker)
        `when`(servicesService.getServiceById(service.id!!)).thenReturn(service)
        `when`(slotService.getSlotById(slot.id!!)).thenReturn(slot)

        // Required mocks for inner call to createBooking(...)
        `when`(slotRepository.findById(slot.id!!)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(seeker.id)).thenReturn(emptyList())
        `when`(bookingRepository.findBySlotId(slot.id!!)).thenReturn(emptyList())
        `when`(bookingRepository.save(any())).thenAnswer { it.arguments[0] as Booking }

        val result = bookingService.createBooking(service.id!!, slot.id!!, isRecurring, username)

        assertEquals(seeker, result.client)
        assertEquals(service, result.service)
        assertEquals(isRecurring, result.isRecurring)
        assertEquals(Status.BOOKED, result.slot.status)
        assertEquals(BookingStatus.PENDING, result.status)
        verify(bookingRepository).save(any())
    }
    @Test
    fun `createBooking should throw when slot is not found`() {
        val username = "seeker"
        val invalidSlotId = 999L

        `when`(seekerService.findByUsername(username)).thenReturn(seeker)
        `when`(servicesService.getServiceById(service.id!!)).thenReturn(service)
        `when`(slotService.getSlotById(invalidSlotId)).thenReturn(null)

        val ex = assertThrows(ResponseStatusException::class.java) {
            bookingService.createBooking(service.id!!, invalidSlotId, isRecurring = false, username)
        }

        assertEquals(HttpStatus.NOT_FOUND, ex.statusCode)
        assertEquals("Slot not found", ex.reason)
    }
    @Test
    fun `createBooking should throw when slot is already BOOKED`() {
        val username = "seeker"
        val bookedSlot = slot.copy(status = Status.BOOKED)

        `when`(seekerService.findByUsername(username)).thenReturn(seeker)
        `when`(servicesService.getServiceById(service.id!!)).thenReturn(service)
        `when`(slotService.getSlotById(bookedSlot.id!!)).thenReturn(bookedSlot)

        `when`(slotRepository.findById(bookedSlot.id!!)).thenReturn(Optional.of(bookedSlot))

        val ex = assertThrows(NotAvailableSlotException::class.java) {
            bookingService.createBooking(service.id!!, bookedSlot.id!!, isRecurring = false, username)
        }

        assertEquals("Slot is not available", ex.message)
    }
    @Test
    fun `createBooking should throw when seeker already booked the same slot`() {
        val username = "seeker"

        // Симулација: постоечки букинг за истиот слот
        val existingBooking = Booking(
            id = 99L,
            createdAt = Instant.now(),
            client = seeker,
            provider = provider,
            service = service,
            slot = slot,
            status = BookingStatus.PENDING,
            isRecurring = false
        )

        `when`(seekerService.findByUsername(username)).thenReturn(seeker)
        `when`(servicesService.getServiceById(service.id!!)).thenReturn(service)
        `when`(slotService.getSlotById(slot.id!!)).thenReturn(slot)

        `when`(slotRepository.findById(slot.id!!)).thenReturn(Optional.of(slot))
        `when`(bookingRepository.findByClientId(seeker.id)).thenReturn(listOf(existingBooking))

        val ex = assertThrows(DuplicateBookingException::class.java) {
            bookingService.createBooking(service.id!!, slot.id!!, isRecurring = false, username)
        }

        assertEquals("You already have a booking for this time slot.", ex.message)

    }
    @Test
    fun `completeBooking should mark as completed and generate next recurring booking`() {
        val originalBooking = Booking(
            id = 1L,
            createdAt = Instant.now(),
            client = seeker,
            provider = provider,
            service = service,
            slot = slot,
            status = BookingStatus.PENDING,
            isRecurring = true
        )

        val completedBooking = originalBooking.copy(status = BookingStatus.COMPLETED)

        val expectedNewSlot = ScheduleSlot(
            id = 200L,
            start_time = slot.start_time.plusSeconds(7 * 24 * 3600),
            end_time = slot.end_time.plusSeconds(7 * 24 * 3600),
            slot_id = slot.id!!,
            status = Status.AVAILABLE,
            created_at = Instant.now(),
            provider = provider
        )

        val expectedNewBooking = Booking(
            id = 999L,
            createdAt = Instant.now(),
            client = seeker,
            provider = provider,
            service = service,
            slot = expectedNewSlot,
            status = BookingStatus.PENDING,
            isRecurring = true
        )

        // mock find original
        `when`(bookingRepository.findById(originalBooking.id!!)).thenReturn(Optional.of(originalBooking))
        // mock save: first for completed, then for new recurring
        `when`(bookingRepository.save(any()))
            .thenReturn(completedBooking)       // first save (complete)
            .thenReturn(expectedNewBooking)     // second save (new recurring)
        `when`(slotRepository.save(any())).thenReturn(expectedNewSlot)

        // act
        val result = bookingService.completeBooking(originalBooking.id!!)

        // assert the original booking was completed
        assertEquals(BookingStatus.COMPLETED, result.status)

        verify(slotRepository).save(argThat { savedSlot ->
            savedSlot.start_time == expectedNewSlot.start_time &&
                    savedSlot.end_time == expectedNewSlot.end_time &&
                    savedSlot.status == Status.AVAILABLE
        })


        // verify both bookings saved
        verify(bookingRepository, times(2)).save(any())

        // verify the new recurring booking is created
        verify(bookingRepository).save(argThat { savedBooking ->
            savedBooking.isRecurring &&
                    savedBooking.status == BookingStatus.PENDING &&
                    savedBooking.slot.start_time == expectedNewSlot.start_time
        })

    }

    @Test
    fun `cancelBooking should cancel all future recurring bookings with same slotId`() {
        val now = Instant.now()
        val recurringSlot = slot.copy(status = Status.BOOKED)
        val originalBooking = Booking(
            id = 1L,
            createdAt = now,
            client = seeker,
            provider = provider,
            service = service,
            slot = recurringSlot,
            status = BookingStatus.PENDING,
            isRecurring = true
        )

        val futureBooking1 = originalBooking.copy(
            id = 2L,
            createdAt = now.plusSeconds(3600),
            slot = recurringSlot.copy(id = 101L, slot_id = slot.slot_id),
            status = BookingStatus.CONFIRMED
        )
        val futureBooking2 = originalBooking.copy(
            id = 3L,
            createdAt = now.plusSeconds(7200),
            slot = recurringSlot.copy(id = 102L, slot_id = slot.slot_id),
            status = BookingStatus.PENDING
        )

        // Return original booking
        `when`(bookingRepository.findById(1L)).thenReturn(Optional.of(originalBooking))

        // Return all bookings for seeker
        `when`(bookingRepository.findByClientId(seeker.id)).thenReturn(
            listOf(originalBooking, futureBooking1, futureBooking2)
        )

        // Mock slot save to return updated slot
        `when`(slotRepository.save(any())).thenAnswer { it.arguments[0] }

        // Act
        val result = bookingService.cancelBooking(1L, cancelAllRecurring = true)

        // Assert original is cancelled
        assertEquals(BookingStatus.CANCELLED, result.status)

        // Verify future bookings are also cancelled
        verify(bookingRepository, times(3)).save(argThat { it.status == BookingStatus.CANCELLED })

        // Verify slots are freed (available)
        verify(slotRepository, times(3)).save(argThat { it.status == Status.AVAILABLE })
    }

    @Test
    fun `cancelBooking should cancel only the selected booking when cancelAllRecurring is false`() {
        val now = Instant.now()
        val recurringSlot = slot.copy(status = Status.BOOKED)
        val bookingToCancel = Booking(
            id = 1L,
            createdAt = now,
            client = seeker,
            provider = provider,
            service = service,
            slot = recurringSlot,
            status = BookingStatus.PENDING,
            isRecurring = true
        )

        val futureBooking = bookingToCancel.copy(
            id = 2L,
            createdAt = now.plusSeconds(7200),
            slot = recurringSlot.copy(id = 102L),
            status = BookingStatus.CONFIRMED
        )

        // Return the booking to cancel
        `when`(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingToCancel))
        `when`(slotRepository.save(any())).thenAnswer { it.arguments[0] }

        // Act
        val result = bookingService.cancelBooking(1L, cancelAllRecurring = false)

        // Assert that only the selected booking is cancelled
        assertEquals(BookingStatus.CANCELLED, result.status)

        // Verify only one booking is saved
        verify(bookingRepository, times(1)).save(argThat { it.id == 1L && it.status == BookingStatus.CANCELLED })

        // Verify only its slot was updated
        verify(slotRepository, times(1)).save(argThat { it.status == Status.AVAILABLE })

        // Ensure no other bookings were fetched or updated
        verify(bookingRepository, never()).findByClientId(any())
    }
    @Test
    fun `completeBooking should only complete the booking when isRecurring is false`() {
        val nonRecurringBooking = Booking(
            id = 1L,
            createdAt = Instant.now(),
            client = seeker,
            provider = provider,
            service = service,
            slot = slot,
            status = BookingStatus.PENDING,
            isRecurring = false
        )

        val completed = nonRecurringBooking.copy(status = BookingStatus.COMPLETED)

        `when`(bookingRepository.findById(nonRecurringBooking.id!!)).thenReturn(Optional.of(nonRecurringBooking))
        `when`(bookingRepository.save(any())).thenReturn(completed)

        val result = bookingService.completeBooking(nonRecurringBooking.id!!)

        assertEquals(BookingStatus.COMPLETED, result.status)
        assertFalse(result.isRecurring)

        // verify only one save (no new recurring booking)
        verify(bookingRepository, times(1)).save(argThat { it.id == 1L && it.status == BookingStatus.COMPLETED })

        // verify no slot was created
        verify(slotRepository, never()).save(any())
    }
    @Test
    fun `completeBooking should complete booking without creating new one if not recurring`() {
        val nonRecurringBooking = Booking(
            id = 1L,
            createdAt = Instant.now(),
            client = seeker,
            provider = provider,
            service = service,
            slot = slot.copy(status = Status.BOOKED),
            status = BookingStatus.PENDING,
            isRecurring = false
        )

        val completedBooking = nonRecurringBooking.copy(status = BookingStatus.COMPLETED)

        `when`(bookingRepository.findById(nonRecurringBooking.id!!))
            .thenReturn(Optional.of(nonRecurringBooking))
        `when`(bookingRepository.save(any()))
            .thenReturn(completedBooking)

        val result = bookingService.completeBooking(nonRecurringBooking.id!!)

        assertEquals(BookingStatus.COMPLETED, result.status)

        // Ensure booking is saved once
        verify(bookingRepository, times(1)).save(argThat { it.status == BookingStatus.COMPLETED })

        // Ensure no recurring booking is saved
        verify(bookingRepository, times(1)).save(any()) // Only the original
        verify(slotRepository, never()).save(any()) // No slot created
    }

    @Test
    fun `createBooking should throw if slot provider doesn't match service provider`() {
        val username = "seeker"
        val otherProvider = provider.copy(id = 99L)
        val mismatchedSlot = slot.copy(provider = otherProvider)

        `when`(seekerService.findByUsername(username)).thenReturn(seeker)
        `when`(servicesService.getServiceById(service.id!!)).thenReturn(service)
        `when`(slotService.getSlotById(slot.id!!)).thenReturn(mismatchedSlot)

        val ex = assertThrows(ResponseStatusException::class.java) {
            bookingService.createBooking(service.id!!, slot.id!!, isRecurring = false, username)
        }

        assertEquals(HttpStatus.BAD_REQUEST, ex.statusCode)
        assertEquals("Slot does not belong to selected service provider", ex.reason)
    }
    @Test
    fun `respondToBooking should confirm booking and reject conflicting bookings`() {
        val pendingBooking = Booking(
            id = 1L, createdAt = Instant.now(), client = seeker, provider = provider,
            service = service, slot = slot.copy(status = Status.AVAILABLE),
            status = BookingStatus.PENDING, isRecurring = false
        )

        val conflict1 = pendingBooking.copy(id = 2L)
        val conflict2 = pendingBooking.copy(id = 3L)

        `when`(bookingRepository.findById(1L)).thenReturn(Optional.of(pendingBooking))
        `when`(bookingRepository.findBySlotId(slot.id!!)).thenReturn(listOf(pendingBooking, conflict1, conflict2))
        `when`(bookingRepository.save(any())).thenAnswer { it.arguments[0] }
        `when`(slotRepository.save(any())).thenReturn(slot)

        val result = bookingService.respondToBooking(1L, accept = true)

        assertEquals(BookingStatus.CONFIRMED, result.status)
        verify(bookingRepository, times(3)).save(any())
        verify(slotRepository).save(argThat { it.status == Status.BOOKED })
    }
    @Test
    fun `respondToBooking should reject booking and free the slot`() {
        val pending = Booking(
            id = 10L, createdAt = Instant.now(), client = seeker, provider = provider,
            service = service, slot = slot.copy(status = Status.BOOKED),
            status = BookingStatus.PENDING, isRecurring = false
        )

        `when`(bookingRepository.findById(10L)).thenReturn(Optional.of(pending))
        `when`(bookingRepository.save(any())).thenAnswer { it.arguments[0] }
        `when`(slotRepository.save(any())).thenAnswer { it.arguments[0] }

        val result = bookingService.respondToBooking(10L, accept = false)

        assertEquals(BookingStatus.REJECTED, result.status)
        verify(bookingRepository).save(argThat { it.status == BookingStatus.REJECTED })
        verify(slotRepository).save(argThat { it.status == Status.AVAILABLE })
    }
    @Test
    fun `respondToBooking should throw when booking not found`() {
        `when`(bookingRepository.findById(999L)).thenReturn(Optional.empty())

        val ex = assertThrows(IllegalArgumentException::class.java) {
            bookingService.respondToBooking(999L, true)
        }

        assertEquals("Booking not found", ex.message)
    }
    @Test
    fun `respondToBooking should throw when booking is not pending`() {
        val confirmedBooking = Booking(
            id = 5L, createdAt = Instant.now(), client = seeker, provider = provider,
            service = service, slot = slot, status = BookingStatus.CONFIRMED, isRecurring = false
        )

        `when`(bookingRepository.findById(5L)).thenReturn(Optional.of(confirmedBooking))


        val ex = assertThrows(NotPendingBookingException::class.java) {
            bookingService.respondToBooking(5L, true)
        }

        assertEquals("Booking with ID 5 cannot respond because is not pending", ex.message)

    }

}
