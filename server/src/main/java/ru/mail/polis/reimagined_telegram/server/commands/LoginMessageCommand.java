package ru.mail.polis.reimagined_telegram.server.commands;

import ru.mail.polis.reimagined_telegram.server.model.User;
import ru.mail.polis.reimagined_telegram.server.model.messages.LoginMessage;
import ru.mail.polis.reimagined_telegram.server.model.messages.Message;
import ru.mail.polis.reimagined_telegram.server.model.messages.StatusMessage;
import ru.mail.polis.reimagined_telegram.server.net.DB;
import ru.mail.polis.reimagined_telegram.server.net.ProtocolException;
import ru.mail.polis.reimagined_telegram.server.net.Session;

import java.io.IOException;


public class LoginMessageCommand implements MessageCommand {

    @Override
    public void execute(Session session, Message message, DB db) throws MessageCommandException {
        LoginMessage loginMessage = (LoginMessage) message;
        String login = loginMessage.getLogin();
        if (!db.getUsers().containsLogin(login)) {
            throw new MessageCommandException("User with same login ( " + login + ") not found");
        }
        String password = loginMessage.getPassword();
        User user = db.getUsers().getUser(login, password);
        session.setUser(user);
        Long userId = user.getId();
        try {
            session.send(new StatusMessage("Logup successful (id=" + userId + "," +
                    " login=" + login + "," +
                    " password=" + password + ")"));
        } catch (ProtocolException | IOException e) {
            e.printStackTrace();
        }
    }
}
