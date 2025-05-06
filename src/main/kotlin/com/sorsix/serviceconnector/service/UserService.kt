package com.sorsix.serviceconnector.service

import com.sorsix.serviceconnector.model.User

interface UserService {
    fun getUserByEmail(email: String) : User?
    fun getUserById(id: Long): User?
}