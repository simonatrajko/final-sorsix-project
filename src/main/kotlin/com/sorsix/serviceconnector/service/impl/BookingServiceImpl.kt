package com.sorsix.serviceconnector.service.impl

import com.sorsix.serviceconnector.exeptions.DuplicateBookingException
import com.sorsix.serviceconnector.exeptions.NotAvailableSlotException
import com.sorsix.serviceconnector.exeptions.NotPendingBookingException
import com.sorsix.serviceconnector.exeptions.ProviderAlreadyBookedException
import com.sorsix.serviceconnector.exeptions.SlotAlreadyConfirmedException
import com.sorsix.serviceconnector.model.Booking
import com.sorsix.serviceconnector.model.BookingStatus
import com.sorsix.serviceconnector.model.ScheduleSlot
import com.sorsix.serviceconnector.model.ServiceSeeker
import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.model.Status
import com.sorsix.serviceconnector.repository.BookingRepository
import com.sorsix.serviceconnector.repository.ScheduleSlotRepository
import com.sorsix.serviceconnector.service.BookingService
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class BookingServiceImpl(
    private val bookingRepository: BookingRepository,
    private val scheduleSlotRepository: ScheduleSlotRepository
): BookingService {

    private val logger = LoggerFactory.getLogger(BookingServiceImpl::class.java)

    @Transactional
    override fun createBooking(
        seeker: ServiceSeeker,
        service: Services,
        slotId: Long,
        isRecurring: Boolean
    ): Booking {
        val slot = getAvailableSlotOrThrow(slotId)

        validateNoDuplicateBooking(seeker, slotId)
        validateSlotNotAlreadyConfirmed(slotId)
        validateProviderAvailability(service.provider.id, slotId)
        val lockedSlot = slot.copy(status = Status.BOOKED)

        val booking = Booking(
            createdAt = Instant.now(),
            client = seeker,
            provider = service.provider,
            service = service,
            slot = lockedSlot,
            status = BookingStatus.PENDING,
            isRecurring = isRecurring
        )
        return bookingRepository.save(booking)
    }

    // Проверува дали има некој букинг што е веќе CONFIRMED и го користи истиот провајдер
    private fun validateProviderAvailability(providerId: Long?, slotId: Long) {
        val bookingsForSlot = bookingRepository.findBySlotId(slotId)
        val conflict = bookingsForSlot.any {
            it.provider.id == providerId && it.status == BookingStatus.CONFIRMED
        }
        if (conflict) {
            throw ProviderAlreadyBookedException()
        }
    }

    private fun getAvailableSlotOrThrow(slotId: Long): ScheduleSlot {
        val slot = scheduleSlotRepository.findById(slotId)
            .orElseThrow { IllegalArgumentException("Slot not found") }

        if (slot.status != Status.AVAILABLE) {
            throw NotAvailableSlotException()
        }

        return slot
    }

    //Дали тој ServiceSeeker (клиент) веќе има букинг за тој слот
    private fun validateNoDuplicateBooking(seeker: ServiceSeeker, slotId: Long) {
        val duplicates = bookingRepository.findByClientId(seeker.id)
            .any { it.slot.id == slotId && it.status != BookingStatus.CANCELLED }
        if (duplicates) {
            throw DuplicateBookingException()
        }
    }

    //    Дали некој букинг за тој слот веќе е CONFIRMED
    private fun validateSlotNotAlreadyConfirmed(slotId: Long) {
        val bookingsForSlot = bookingRepository.findBySlotId(slotId)
        val isAlreadyConfirmed = bookingsForSlot.any { it.status == BookingStatus.CONFIRMED }
        if (isAlreadyConfirmed) {
            throw SlotAlreadyConfirmedException(slotId)
        }
    }

    @Transactional
    override fun respondToBooking(bookingId: Long, accept: Boolean): Booking {
        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { IllegalArgumentException("Booking not found") }

        if (booking.status != BookingStatus.PENDING) {
            throw NotPendingBookingException(bookingId)
        }

        if (accept) {
            confirmBooking(booking)
            rejectConflictingBookings(booking)
        } else {
            booking.status = BookingStatus.REJECTED
            scheduleSlotRepository.save(booking.slot.copy(status = Status.AVAILABLE))
        }

        bookingRepository.save(booking)
        return booking
    }

    private fun confirmBooking(booking: Booking) {
        booking.status = BookingStatus.CONFIRMED
        booking.slot.status = Status.BOOKED
        scheduleSlotRepository.save(booking.slot)
    }

    private fun rejectConflictingBookings(confirmedBooking: Booking) {
        val slotId = confirmedBooking.slot.id!!
        val bookingId = confirmedBooking.id!!

        val conflictingBookings = bookingRepository.findBySlotId(slotId)
            .filter { it.id != bookingId && it.status == BookingStatus.PENDING }

        conflictingBookings.forEach { booking ->
            booking.status = BookingStatus.REJECTED
            bookingRepository.save(booking)
        }

        if (conflictingBookings.isNotEmpty()) {
            logger.info("Rejected \${conflictingBookings.size} conflicting bookings for slot \$slotId")
        }
    }


    override fun getBookingsForProvider(providerId: Long): List<Booking> =
        bookingRepository.findByProviderId(providerId)

    override fun getBookingsForSeeker(seekerId: Long): List<Booking> =
        bookingRepository.findByClientId(seekerId)

    @Transactional
    override fun cancelBooking(bookingId: Long, cancelAllRecurring: Boolean): Booking {
        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { IllegalArgumentException("Booking not found") }

        booking.status = BookingStatus.CANCELLED
        bookingRepository.save(booking)

        if (booking.slot.status == Status.BOOKED) {
            freeUpSlot(booking.slot)
        }

        if (cancelAllRecurring && booking.isRecurring) {
            cancelAllFutureRecurringBookings(booking)
        }

        return booking
    }

    private fun freeUpSlot(slot: ScheduleSlot) {
        val updated = slot.copy(status = Status.AVAILABLE)
        scheduleSlotRepository.save(updated)
    }

    private fun cancelAllFutureRecurringBookings(original: Booking) {
        val seekerId = original.client.id
        val slotGroupId = original.slot.slot_id

        val relatedBookings = bookingRepository.findByClientId(seekerId)
            .filter {
                it.slot.slot_id == slotGroupId &&
                        it.status != BookingStatus.CANCELLED &&
                        it.status != BookingStatus.COMPLETED &&
                        it.createdAt.isAfter(original.createdAt)
            }

        relatedBookings.forEach {
            it.status = BookingStatus.CANCELLED
            bookingRepository.save(it)

            if (it.slot.status == Status.BOOKED) {
                freeUpSlot(it.slot)
            }
        }
    }

    @Transactional
    override fun completeBooking(bookingId: Long): Booking {
        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { IllegalArgumentException("Booking not found") }

        booking.status = BookingStatus.COMPLETED
        val completedBooking = bookingRepository.save(booking)

        if (!booking.isRecurring) return completedBooking

        val nextSlot = createNextSlot(booking)
        val newBooking = createNextRecurringBooking(booking, nextSlot)

        bookingRepository.save(newBooking)

        return completedBooking
    }

    private fun createNextSlot(booking: Booking): ScheduleSlot {
        val nextStart = booking.slot.start_time.plusSeconds(7 * 24 * 3600)
        val nextEnd = booking.slot.end_time.plusSeconds(7 * 24 * 3600)

        val newSlot = ScheduleSlot(
            start_time = nextStart,
            end_time = nextEnd,
            slot_id = booking.slot.id!!,
            status = Status.AVAILABLE,
            created_at = Instant.now(),
            provider = booking.provider
        )
        return scheduleSlotRepository.save(newSlot)
    }

    private fun createNextRecurringBooking(booking: Booking, slot: ScheduleSlot): Booking =
        Booking(
            createdAt = Instant.now(),
            client = booking.client,
            provider = booking.provider,
            service = booking.service,
            slot = slot,
            status = BookingStatus.PENDING,
            isRecurring = true
        )

}
