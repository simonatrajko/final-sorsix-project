package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.model.ServiceProvider
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceProviderRepository : JpaRepository<ServiceProvider,Long> {
    fun findByUsername(username: String): ServiceProvider?
}