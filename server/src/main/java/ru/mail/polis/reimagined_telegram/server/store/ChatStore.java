package ru.mail.polis.reimagined_telegram.server.store;

import ru.mail.polis.reimagined_telegram.server.model.Chat;

import java.io.FileNotFoundException;
import java.util.List;


public interface ChatStore {

    Chat getChatById(Long id);

    List<Long> getChatsByUser(Long userId);

    List<Long> getUsersByChat(Long chatId);

    List<Long> getChatMessages(Long chatId);

    void addNewChat(Chat chat);

    void addUserToChat(Long userId, Long chatId) throws FileNotFoundException;

    void removeUserFromChat(Long userId, Long chatId);

    void addMessageToChat(Long messageId, Long chatId) throws FileNotFoundException;

    void removeMessageFromChat(Long messageId, Long chatId);

    boolean chatExists(Long chatId);
}
