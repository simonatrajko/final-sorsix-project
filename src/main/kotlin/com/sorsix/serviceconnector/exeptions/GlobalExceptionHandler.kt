package com.sorsix.serviceconnector.exeptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapOf("error" to "Access denied"))

    @ExceptionHandler(DuplicateBookingException::class)
    fun handleDuplicateBooking(ex: DuplicateBookingException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(NotAvailableSlotException::class)
    fun handleNotAvailableSlot(ex: NotAvailableSlotException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(SlotAlreadyConfirmedException::class)
    fun handleSlotAlreadyConfirmed(ex: SlotAlreadyConfirmedException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(ProviderAlreadyBookedException::class)
    fun handleProviderAlreadyBooked(ex: ProviderAlreadyBookedException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(NotPendingBookingException::class)
    fun handleNotPendingBooking(ex: NotPendingBookingException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to ex.message.orEmpty()))
}

