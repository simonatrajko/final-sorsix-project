package com.sorsix.serviceconnector.model

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("PROVIDER")
class ServiceProvider(
    username: String,
    hashedPassword: String,
    email: String,
    fullName: String,
    profileImage: String,
    location: String,

    // Provider-specific fields
    var yearsOfExperience: Int? = null,
    var bio: String? = null,
    var languages: String? = null
) : User(
    username = username,
    hashedPassword = hashedPassword,
    email = email,
    fullName = fullName,
    profileImage = profileImage,
    location = location
)
