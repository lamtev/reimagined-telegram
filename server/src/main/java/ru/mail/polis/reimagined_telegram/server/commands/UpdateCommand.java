package ru.mail.polis.reimagined_telegram.server.commands;

import ru.mail.polis.reimagined_telegram.server.model.messages.Message;
import ru.mail.polis.reimagined_telegram.server.net.DB;
import ru.mail.polis.reimagined_telegram.server.net.Session;

public class UpdateCommand implements MessageCommand {

    @Override
    public void execute(Session session, Message message, DB db) throws MessageCommandException {
        //TODO
    }
}
