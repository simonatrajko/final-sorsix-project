package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService (private val userRepository: UserRepository){
}