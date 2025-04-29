package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.ServiceSeeker
import com.sorsix.serviceconnector.repository.ServiceSeekerRepository
import org.springframework.stereotype.Service

@Service
class ServiceSeekerService(private val serviceSeekerRepository: ServiceSeekerRepository){
    fun findById(id: Long): ServiceSeeker =
        serviceSeekerRepository.findById(id).orElseThrow { RuntimeException("Seeker not found") }

    fun findByUsername(username: String): ServiceSeeker? =
        serviceSeekerRepository.findByUsername(username)

    fun getAllSeekers(): List<ServiceSeeker> =
        serviceSeekerRepository.findAll()

    fun saveSeeker(seeker: ServiceSeeker): ServiceSeeker =
        serviceSeekerRepository.save(seeker)
}
