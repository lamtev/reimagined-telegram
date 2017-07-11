package ru.mail.polis.reimagined_telegram.server.commands;


import ru.mail.polis.reimagined_telegram.server.model.messages.Message;
import ru.mail.polis.reimagined_telegram.server.net.DB;
import ru.mail.polis.reimagined_telegram.server.net.Session;


public class InfoMessageCommand implements MessageCommand {
    @Override
    public void execute(Session session, Message msg, DB db) throws MessageCommandException {
        //TODO
    }
}
