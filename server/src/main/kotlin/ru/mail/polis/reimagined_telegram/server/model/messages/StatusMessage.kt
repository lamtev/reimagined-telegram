package ru.mail.polis.reimagined_telegram.server.model.messages

import ru.mail.polis.reimagined_telegram.server.model.messages.Type.MSG_STATUS

data class StatusMessage(var message: String) : Message() {

    init {
        type = MSG_STATUS
    }
}
