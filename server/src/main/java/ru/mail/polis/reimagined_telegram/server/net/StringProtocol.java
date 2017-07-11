package ru.mail.polis.reimagined_telegram.server.net;

import ru.mail.polis.reimagined_telegram.server.model.messages.*;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringProtocol implements Protocol {

    private static final String DELIMITER = ";";

    @Override
    public Message decode(byte[] bytes) throws ProtocolException {
        String str = new String(bytes);
        System.out.println("decoded: " + str);
        String[] tokens = str.split(DELIMITER);
        System.out.println(tokens[0]);
        Type type = Type.valueOf(tokens[0]);
        switch (type) {
            case MSG_LOGUP:
                return new LogupMessage(tokens[1], tokens[2]);
            case MSG_LOGIN:
                return new LoginMessage(tokens[1], tokens[2]);
            case MSG_CHAT_CREATE:
                return new CreateChatMessage(
                        Arrays.stream(tokens[1].split(","))
                                .map(Long::valueOf)
                                .collect(Collectors.toList())
                );
            case MSG_TEXT:
                TextMessage textMessage = new TextMessage();
                textMessage.setDestination(parseLong(tokens[1]));
                textMessage.setText(tokens[2]);
                return textMessage;
            case MSG_STATUS:
                return new StatusMessage(tokens[1]);
            default:
                throw new ProtocolException("Invalid type: " + type);
        }
    }

    @Override
    public byte[] encode(Message message) throws ProtocolException {
        StringBuilder builder = new StringBuilder();
        Type type = message.getType();
        builder.append(type).append(DELIMITER);
        assert type != null;
        switch (type) {
            case MSG_LOGUP:
                LogupMessage logupMessage = (LogupMessage) message;
                builder.append(logupMessage.getLogin())
                        .append(DELIMITER)
                        .append(logupMessage.getPassword());
                break;
            case MSG_LOGIN:
                LoginMessage loginMessage = (LoginMessage) message;
                builder.append(loginMessage.getLogin())
                        .append(DELIMITER)
                        .append(loginMessage.getPassword());
                break;
            case MSG_CHAT_CREATE:
                CreateChatMessage createChatMessage = (CreateChatMessage) message;
                StringBuilder sb = new StringBuilder();
                createChatMessage.getInterlocutors().forEach(id -> sb.append(id).append(","));
                sb.deleteCharAt(sb.lastIndexOf(","));
                builder.append(sb.toString());
                break;
            case MSG_TEXT:
                TextMessage textMessage = (TextMessage) message;
                builder.append(String.valueOf(textMessage.getDestination())).append(DELIMITER);
                builder.append(textMessage.getText()).append(DELIMITER);
                break;
            case MSG_STATUS:
                StatusMessage statusMessage = (StatusMessage) message;
                builder.append(statusMessage.getMessage());
                break;
            default:
                throw new ProtocolException("Invalid type: " + type);

        }
        System.out.println("encoded: " + builder);
        return builder.toString().getBytes();
    }

    private Long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            // who care
        }
        return null;
    }
}