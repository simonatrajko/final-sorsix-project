package com.sorsix.serviceconnector.exeptions

import java.lang.Exception

class DuplicateBookingException : Exception {
constructor():super("You already have a booking for this time slot.")
}