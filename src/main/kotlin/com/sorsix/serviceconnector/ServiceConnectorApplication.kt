package com.sorsix.serviceconnector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServiceConnectorApplication

fun main(args: Array<String>) {
    runApplication<ServiceConnectorApplication>(*args)
}
