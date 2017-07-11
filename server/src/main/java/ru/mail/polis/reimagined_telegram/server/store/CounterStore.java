package ru.mail.polis.reimagined_telegram.server.store;

public interface CounterStore {

    Long newChatId();

    Long newMessageId();

    Long newUserId();

}
