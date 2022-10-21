package com.example.domain.model

class UserProfileData(
    var uid: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var username: String? = null,
    var imageStr: String? = null,
    /*var contactEmail: String = "",
    var gitHub: String = "",
    var about: String = ""*/

    ) {

    constructor(): this (uid = "")
}