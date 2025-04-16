package com.sorsix.serviceconnector.model

//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "user_type")
abstract class User(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var username: String,
    var password: String,
    var email: String,
    var fullName: String,
    var profileImage: String,
    var location: String,

//    @Column(insertable = false, updatable = false)
    val userType: String? = null
)