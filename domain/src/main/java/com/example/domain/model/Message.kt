package com.example.domain.model

data class Message(
    var uid: String = "",
    var message: String = "",
    var icon: String? = null,
    var username: String = "Dave") {

    constructor() :this("")
}