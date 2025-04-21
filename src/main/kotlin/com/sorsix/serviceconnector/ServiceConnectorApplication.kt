package com.sorsix.serviceconnector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ServiceConnectorApplication

fun main(args: Array<String>) {
    runApplication<ServiceConnectorApplication>(*args)
}
