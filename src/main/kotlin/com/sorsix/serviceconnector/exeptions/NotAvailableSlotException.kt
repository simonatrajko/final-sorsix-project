package com.sorsix.serviceconnector.exeptions

class NotAvailableSlotException: Exception {
    constructor() : super("Slot is not available")
}