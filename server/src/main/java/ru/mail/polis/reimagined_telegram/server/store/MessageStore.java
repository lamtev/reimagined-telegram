package ru.mail.polis.reimagined_telegram.server.store;

import ru.mail.polis.reimagined_telegram.server.model.messages.Message;

import java.io.FileNotFoundException;


public interface MessageStore {

    Message getMessageById(Long messageId) throws FileNotFoundException;

    Long addMessage(Message message);

    void removeMessage(Long messageId) throws FileNotFoundException;

}
