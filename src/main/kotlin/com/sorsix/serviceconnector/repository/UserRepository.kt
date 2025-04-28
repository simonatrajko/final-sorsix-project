package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}