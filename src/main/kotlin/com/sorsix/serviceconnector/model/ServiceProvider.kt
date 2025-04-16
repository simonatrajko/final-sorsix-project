package com.sorsix.serviceconnector.model


//@Entity
//@DiscriminatorValue("PROVIDER")
class ServiceProvider(
    username: String,
    password: String,
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
    password = password,
    email = email,
    fullName = fullName,
    profileImage = profileImage,
    location = location
)
