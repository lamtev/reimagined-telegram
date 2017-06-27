package ru.mail.polis.server.net;

import ru.mail.polis.server.model.Message;

public interface Protocol {

    Message decode(byte[] bytes) throws ProtocolException;

    byte[] encode(Message msg) throws ProtocolException;

}
