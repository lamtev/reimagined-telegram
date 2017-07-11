package ru.mail.polis.reimagined_telegram.server.model.messages


data class LogupMessage(var login: String, var password: String) : Message() {
    init {
        type = Type.MSG_LOGUP
    }
}