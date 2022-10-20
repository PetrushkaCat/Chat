package com.example.domain.model

data class Message(var uid: String = "", var message: String = "", var icon: String = "no image", var username: String = "no name") {

    constructor() :this("")
}