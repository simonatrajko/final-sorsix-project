package com.sorsix.serviceconnector.exeptions

class ProviderAlreadyBookedException : Exception {
    constructor() : super("The provider is already booked in this time slot.")
}