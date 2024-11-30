package com.example.chatapplication

class Message {
    var message: String? = "null"
    var sendername: String? = "null"
    var imagepath: String? = "null"

    constructor(){}

    constructor(message: String?, sendername: String?, imagepath: String) {
        this.message = message
        this.sendername = sendername
        this.imagepath = imagepath
    }


}