package com.sorsix.serviceconnector.exeptions

class NotPendingBookingException : Exception {
    constructor(bookingId: Long) : super("Booking with ID $bookingId cannot respond because is not pending")
}