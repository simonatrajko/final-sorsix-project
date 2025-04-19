package com.sorsix.serviceconnector.model

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("SEEKER")
 class ServiceSeeker(
    username: String,
    password: String,
    email: String,
    fullName: String,
    profileImage: String,
    location: String,
    // Seeker-specific fields
    var preferredContactMethod: String? = null,
    var notificationPreferences: String? = null
) : User(
    username = username,
    password = password,
    email = email,
    fullName = fullName,
    profileImage = profileImage,
    location = location
)