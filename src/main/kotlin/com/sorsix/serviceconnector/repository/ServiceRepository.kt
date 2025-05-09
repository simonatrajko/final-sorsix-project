package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.ServiceCategory
import com.sorsix.serviceconnector.model.Services
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceRepository: JpaRepository<Services, Long> {
    fun findAllByCategory(category: ServiceCategory): List<Services>

    fun findAllByProvider_Id(providerId: Long): List<Services>

    fun findAllByCategory_NameIgnoreCase(categoryName: String): List<Services>
}