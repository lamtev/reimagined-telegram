package ru.mail.polis.reimagined_telegram.server.commands;


import ru.mail.polis.reimagined_telegram.server.model.messages.Type;

public class MessageCommandFactory {

    public static MessageCommand getCommand(Type type) {
        switch (type) {
            case MSG_TEXT:
                return new TextMessageCommand();
            case MSG_INFO:
                return new InfoMessageCommand();
            case MSG_LOGIN:
                return new LoginMessageCommand();
            case MSG_LOGUP:
                return new LogupMessageCommand();
            case MSG_CHAT_CREATE:
                return new CreateChatCommand();
            case MSG_CHAT_LIST:
                return new ChatListCommand();
            case MSG_CHAT_HIST:
                return new ChatHistoryCommand();
            case MSG_UPD:
                return new UpdateCommand();
            default:
                throw new RuntimeException();
        }
    }
}
