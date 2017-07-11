package ru.mail.polis.reimagined_telegram.server.store;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.mail.polis.reimagined_telegram.server.model.Chat;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class MongodbChatStore implements ChatStore {

    private static final Gson GSON = new Gson();
    private MongoCollection<Document> chats;
    private MongoCollection<Document> counters;

    public MongodbChatStore(MongoDatabase db) {
        this.chats = db.getCollection("Chats");
        this.counters = db.getCollection("Counters");
    }

    @Override
    public Chat getChatById(Long id) {
        String chatJson = chats.find(eq("id", id)).first().toJson();
        return GSON.fromJson(chatJson, Chat.class);
    }

    @Override
    public List<Long> getChatsByUser(Long userId) {
        return chats.find().into(new ArrayList<>()).parallelStream()
                .filter(chat -> {
                    String jsonChat = chat.toJson();
                    Chat chatObject = GSON.fromJson(jsonChat, Chat.class);
                    return chatObject.getParticipants().contains(userId);
                })
                .map(chat -> chat.getLong("id"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getUsersByChat(Long chatId) {
        return null;
    }

    @Override
    public List<Long> getChatMessages(Long chatId) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addUserToChat(Long userId, Long chatId) throws FileNotFoundException {
        Document chat = chats.find(eq("id", chatId)).first();
        if (chat == null) {
            throw new FileNotFoundException();
        }
        chat.get("participants", ArrayList.class).add(userId);
    }

    @Override
    public void removeUserFromChat(Long userId, Long chatId) {
        //TODO
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addMessageToChat(Long messageId, Long chatId) throws FileNotFoundException {
        Document chat = chats.find(eq("id", chatId)).first();
        if (chat == null) {
            throw new FileNotFoundException();
        }
        chat.get("messages", ArrayList.class).add(messageId);
    }

    @Override
    public void removeMessageFromChat(Long messageId, Long chatId) {
        //TODO
    }

    @Override
    public void addNewChat(Chat chat) {
        String chatJson = GSON.toJson(chat);
        chats.insertOne(Document.parse(chatJson));
    }

    @Override
    public boolean chatExists(Long chatId) {
        return chats.find(eq("id", chatId)).first() != null;
    }
}
