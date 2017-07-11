package ru.mail.polis.reimagined_telegram.server.net;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import ru.mail.polis.reimagined_telegram.server.store.*;

@SuppressWarnings("WeakerAccess")
public class DB {

    private final UserStore users;
    private final MessageStore messages;
    private final ChatStore chats;
    private final CounterStore counters;

    private DB() {
        MongoDatabase db = new MongoClient("localhost", 27017)
                .getDatabase("reimagined-telegram");
        users = new MongodbUserStore(db);
        messages = new MongodbMessageStore(db);
        chats = new MongodbChatStore(db);
        counters = new MongodbCounterStore(db);
    }

    public static DB getInstance() {
        return DBHolder.instance;
    }

    public CounterStore getCounters() {
        return counters;
    }

    public UserStore getUsers() {
        return users;
    }

    public MessageStore getMessages() {
        return messages;
    }

    public ChatStore getChats() {
        return chats;
    }

    private static class DBHolder {
        private static final DB instance = new DB();
    }

}
