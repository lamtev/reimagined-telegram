package ru.mail.polis.reimagined_telegram.server.model.messages

import ru.mail.polis.reimagined_telegram.server.model.messages.Type.MSG_CHAT_CREATE


data class CreateChatMessage(var interlocutors: List<Long>) : Message() {
    init {
        type = MSG_CHAT_CREATE
    }
}
