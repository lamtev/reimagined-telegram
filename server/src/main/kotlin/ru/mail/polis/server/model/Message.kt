package ru.mail.polis.server.model

import java.io.Serializable

abstract class Message : Serializable {

    var id: Long? = null
    var senderId: Long? = null
    var type: Type? = null

}
