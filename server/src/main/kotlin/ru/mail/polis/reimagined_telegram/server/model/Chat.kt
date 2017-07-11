package ru.mail.polis.reimagined_telegram.server.model


class Chat(var id: Long,
           var admin: Long,
           val participants: List<Long> = ArrayList()) {

    val messages: List<Long> = ArrayList()

    fun addMessage(message: Long) {
        messages.plus(message)
    }

    fun addParticipant(participant: Long) {
        participants.plus(participant)
    }

    fun removeParticipant(participant: Long) {
        participants.minus(participant)
    }

}