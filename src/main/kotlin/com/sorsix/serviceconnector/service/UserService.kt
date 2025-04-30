package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService (private val userRepository: UserRepository){
    fun getUserByEmail(email: String) = userRepository.findByEmail(email)
    fun getUserById(id: Long) = userRepository.findById(id)
}