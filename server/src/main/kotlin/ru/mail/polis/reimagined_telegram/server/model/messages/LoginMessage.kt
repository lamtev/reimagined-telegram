package ru.mail.polis.reimagined_telegram.server.model.messages


data class LoginMessage(var login: String? = null, var password: String? = null) : Message() {
    init {
        type = Type.MSG_LOGIN
    }
}
