package com.sorsix.serviceconnector.service.impl

import com.sorsix.serviceconnector.model.ServiceProvider
import com.sorsix.serviceconnector.model.ServiceSeeker
import com.sorsix.serviceconnector.repository.ServiceSeekerRepository
import com.sorsix.serviceconnector.service.ServiceSeekerService
import org.springframework.stereotype.Service

@Service
class ServiceSeekerServiceImpl(private val serviceSeekerRepository: ServiceSeekerRepository)
    : ServiceSeekerService{
    override fun findById(id: Long): ServiceSeeker =
        serviceSeekerRepository.findById(id).orElseThrow { RuntimeException("Seeker not found") }

    override fun findByUsername(username: String): ServiceSeeker? =
        serviceSeekerRepository.findByUsername(username)

    override fun getAllSeekers(): List<ServiceSeeker> =
        serviceSeekerRepository.findAll()

    override fun saveSeeker(seeker: ServiceSeeker): ServiceSeeker =
        serviceSeekerRepository.save(seeker)
}
