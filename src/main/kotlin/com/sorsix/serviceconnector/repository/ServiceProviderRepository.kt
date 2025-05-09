package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.Services
import com.sorsix.serviceconnector.model.ServiceProvider
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ServiceProviderRepository : JpaRepository<ServiceProvider,Long> {
    fun findByUsername(username: String): ServiceProvider?
    fun findByFullNameContainingIgnoreCase(name: String): List<ServiceProvider>
}