package ru.mail.polis.reimagined_telegram.server.store;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class MongodbCounterStore implements CounterStore {

    private MongoCollection<Document> counters;

    public MongodbCounterStore(MongoDatabase db) {
        counters = db.getCollection("Counters");
    }

    @Override
    public Long newChatId() {
        return newId("chatId");
    }

    @Override
    public Long newMessageId() {
        return newId("messageId");
    }

    @Override
    public Long newUserId() {
        return newId("userId");
    }

    private Long newId(String idType) {
        if (counters.find(eq("id", idType)).first() == null) {
            counters.insertOne(
                    new Document()
                            .append("id", new BsonString(idType))
                            .append("counter", new BsonInt64(0)));
        }
        Long id = counters.find(eq("id", idType)).first().getLong("counter");
        counters.updateOne(
                eq("id", idType),
                new Document("$set", new Document("counter", new BsonInt64(id + 1)))
        );
        return id;
    }

}
