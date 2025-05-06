package com.sorsix.serviceconnector.service.impl

import com.sorsix.serviceconnector.repository.UserRepository
import com.sorsix.serviceconnector.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl (private val userRepository: UserRepository): UserService {
    override fun getUserByEmail(email: String) = userRepository.findByEmail(email)
    override fun getUserById(id: Long)= userRepository.findById(id).orElseThrow { RuntimeException("User not found") }
}