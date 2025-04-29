package com.sorsix.serviceconnector.exeptions

class SlotAlreadyConfirmedException : Exception {
    constructor(slotId: Long): super("Slot with ID $slotId is already confirmed for another booking.")
}