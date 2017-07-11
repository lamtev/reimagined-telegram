package ru.mail.polis.reimagined_telegram.server.model.messages

import ru.mail.polis.reimagined_telegram.server.model.messages.Type.MSG_TEXT

data class TextMessage(var destination: Long? = null, var text: String? = null) : Message() {
    init {
        type = MSG_TEXT
    }
}
