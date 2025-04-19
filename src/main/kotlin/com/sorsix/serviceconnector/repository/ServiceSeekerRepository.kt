package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.ServiceSeeker
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceSeekerRepository : JpaRepository<ServiceSeeker, Long> {
    fun findByUsername(username: String): ServiceSeeker?
}