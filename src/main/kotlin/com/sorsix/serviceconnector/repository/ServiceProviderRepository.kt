package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.ServiceProvider
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceProviderRepository : JpaRepository<ServiceProvider,Long> {
    fun findByUsername(username: String): ServiceProvider?
    fun findByFullNameContainingIgnoreCase(name: String): List<ServiceProvider>
}