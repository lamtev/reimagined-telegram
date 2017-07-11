package ru.mail.polis.reimagined_telegram.server.commands;


import ru.mail.polis.reimagined_telegram.server.model.Chat;
import ru.mail.polis.reimagined_telegram.server.model.messages.CreateChatMessage;
import ru.mail.polis.reimagined_telegram.server.model.messages.Message;
import ru.mail.polis.reimagined_telegram.server.model.messages.StatusMessage;
import ru.mail.polis.reimagined_telegram.server.net.DB;
import ru.mail.polis.reimagined_telegram.server.net.ProtocolException;
import ru.mail.polis.reimagined_telegram.server.net.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CreateChatCommand implements MessageCommand {
    @Override
    public void execute(Session session, Message message, DB db) throws MessageCommandException {
        CreateChatMessage createChatMessage = (CreateChatMessage) message;
        List<Long> interlocutors = createChatMessage.getInterlocutors();

        for (Long userId : interlocutors) {
            if (!db.getUsers().containsId(userId)) {
                try {
                    session.send(new StatusMessage("No user with id=" + userId));
                    return;
                } catch (ProtocolException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        List<Long> chatParticipants = new ArrayList<>();
        chatParticipants.addAll(interlocutors);
        chatParticipants.add(session.getUser().getId());
        Long chatId = db.getCounters().newChatId();
        Long adminId = session.getUser().getId();
        Chat chat = new Chat(chatId, adminId, chatParticipants);
        db.getChats().addNewChat(chat);
        try {
            session.send(new StatusMessage("Chat successfully created (id=" + chatId + ")"));
        } catch (ProtocolException | IOException e) {
            e.printStackTrace();
        }

    }
}
