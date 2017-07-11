package ru.mail.polis.reimagined_telegram.server.store;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.mail.polis.reimagined_telegram.server.model.User;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongodbUserStore implements UserStore {

    private MongoCollection<Document> users;

    public MongodbUserStore(MongoDatabase db) {
        users = db.getCollection("Users");
    }

    @Override
    public User addUser(User user) {
        String userJson = new Gson().toJson(user);
        users.insertOne(Document.parse(userJson));
        return user;
    }

    @Override
    public User updateUser(User user) {
        String userJson = new Gson().toJson(user);
        users.updateOne(eq("id", user.getId()), Document.parse(userJson));
        return user;
    }

    @Override
    public User getUser(String login, String pass) {
        String userJson = users.find(and(
                eq("login", login),
                eq("password", pass))
        ).first().toJson();
        return new Gson().fromJson(userJson, User.class);
    }

    @Override
    public User getUserById(Long id) {
        String userJson = users.find(eq("id", id)).first().toJson();
        return new Gson().fromJson(userJson, User.class);
    }

    @Override
    public boolean containsLogin(String login) {
        return users.find(eq("login", login)).first() != null;
    }

    @Override
    public boolean containsId(Long id) {
        return users.find(eq("id", id)).first() != null;
    }
}
