package ru.mail.polis.reimagined_telegram.server.commands;

import ru.mail.polis.reimagined_telegram.server.model.User;
import ru.mail.polis.reimagined_telegram.server.model.messages.LogupMessage;
import ru.mail.polis.reimagined_telegram.server.model.messages.Message;
import ru.mail.polis.reimagined_telegram.server.model.messages.StatusMessage;
import ru.mail.polis.reimagined_telegram.server.net.DB;
import ru.mail.polis.reimagined_telegram.server.net.ProtocolException;
import ru.mail.polis.reimagined_telegram.server.net.Session;

import java.io.IOException;

public class LogupMessageCommand implements MessageCommand {
    @Override
    public void execute(Session session, Message message, DB db) throws MessageCommandException {
        LogupMessage logupMessage = (LogupMessage) message;
        String login = logupMessage.getLogin();
        if (db.getUsers().containsLogin(login)) {
            throw new MessageCommandException("User with same login (" + login + ") already exists");
        }
        Long userId = db.getCounters().newUserId();
        String password = logupMessage.getPassword();
        User user = new User(userId, login, password);
        db.getUsers().addUser(user);
        session.setUser(user);
        try {
            session.send(new StatusMessage("Logup successful (id=" + userId + "," +
                    " login=" + login + "," +
                    " password=" + password + ")"));
        } catch (ProtocolException | IOException e) {
            e.printStackTrace();
        }
    }
}
