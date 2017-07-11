package ru.mail.polis.reimagined_telegram.server.model.messages

import java.io.Serializable


abstract class Message(var id: Long? = null,
                       var senderId: Long? = null,
                       var type: Type? = null) : Serializable