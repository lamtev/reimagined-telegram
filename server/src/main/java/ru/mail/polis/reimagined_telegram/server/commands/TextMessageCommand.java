package ru.mail.polis.reimagined_telegram.server.commands;


import ru.mail.polis.reimagined_telegram.server.model.messages.Message;
import ru.mail.polis.reimagined_telegram.server.model.messages.StatusMessage;
import ru.mail.polis.reimagined_telegram.server.model.messages.TextMessage;
import ru.mail.polis.reimagined_telegram.server.net.DB;
import ru.mail.polis.reimagined_telegram.server.net.ProtocolException;
import ru.mail.polis.reimagined_telegram.server.net.Session;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TextMessageCommand implements MessageCommand {

    @Override
    public void execute(Session session, Message message, DB db) throws MessageCommandException {
        TextMessage textMessage = (TextMessage) message;
        Long chatId = textMessage.getDestination();
        if (!db.getChats().chatExists(chatId)) {
            throw new MessageCommandException("Chat with id=" + chatId + " not found");
        }
        Long messageId = db.getCounters().newMessageId();
        textMessage.setId(messageId);
        textMessage.setSenderId(session.getUser().getId());
        db.getMessages().addMessage(textMessage);
        try {
            db.getChats().addMessageToChat(messageId, textMessage.getDestination());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            session.send(new StatusMessage(textMessage.getText()));
        } catch (ProtocolException | IOException e) {
            e.printStackTrace();
        }

    }
}
