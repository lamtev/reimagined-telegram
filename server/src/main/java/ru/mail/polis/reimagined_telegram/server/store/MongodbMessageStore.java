package ru.mail.polis.reimagined_telegram.server.store;

import com.google.gson.Gson;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.mail.polis.reimagined_telegram.server.model.messages.Message;

import java.io.FileNotFoundException;

import static com.mongodb.client.model.Filters.eq;

public class MongodbMessageStore implements MessageStore {

    private MongoCollection<Document> messages;
    private MongoCollection<Document> counters;
    private static final Gson GSON = new Gson();

    public MongodbMessageStore(MongoDatabase db) {
        messages = db.getCollection("Messages");
        counters = db.getCollection("Counters");
    }

    @Override
    public Message getMessageById(Long messageId) throws FileNotFoundException {
        Document msg = messages.find(eq("id", messageId)).first();
        if (msg == null) {
            throw new FileNotFoundException();
        }
        String messageJson = msg.toJson();
        return GSON.fromJson(messageJson, Message.class);
    }

    @Override
    public Long addMessage(Message message) throws MongoException {

        String messageJson = GSON.toJson(message);
        messages.insertOne(Document.parse(messageJson));
        return message.getId();
    }

    @Override
    public void removeMessage(Long messageId) throws MongoException {
        messages.deleteOne(eq("id", messageId));
    }
}
