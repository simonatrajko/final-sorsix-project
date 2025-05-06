package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.ServiceSeeker

interface ServiceSeekerService {
    fun findById(id: Long): ServiceSeeker?
    fun findByUsername(username: String): ServiceSeeker?
    fun getAllSeekers(): List<ServiceSeeker>
    fun saveSeeker(seeker: ServiceSeeker): ServiceSeeker
}