package ru.mail.polis.reimagined_telegram.server.net;

import ru.mail.polis.reimagined_telegram.server.model.messages.Message;

public interface Protocol {

    Message decode(byte[] bytes) throws ProtocolException;

    byte[] encode(Message msg) throws ProtocolException;

}
