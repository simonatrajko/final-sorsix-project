package com.sorsix.serviceconnector.repository

import com.sorsix.serviceconnector.model.ServiceCategory
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceCategoryRepository : JpaRepository<ServiceCategory, Long> {
}