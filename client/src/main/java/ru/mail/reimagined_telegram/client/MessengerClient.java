package ru.mail.reimagined_telegram.client;

import ru.mail.polis.reimagined_telegram.server.model.messages.*;
import ru.mail.polis.reimagined_telegram.server.net.Protocol;
import ru.mail.polis.reimagined_telegram.server.net.ProtocolException;
import ru.mail.polis.reimagined_telegram.server.net.StringProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MessengerClient {

    private static final Protocol protocol = new StringProtocol();
    private static final int port = 19000;
    private static final String host = "localhost";
    private InputStream in;
    private OutputStream out;

    public static void main(String[] args) throws Exception {

        MessengerClient client = new MessengerClient();

        try {
            client.initSocket();

            Scanner scanner = new Scanner(System.in);
            System.out.println("$");
            while (true) {
                String input = scanner.nextLine();
                if ("q".equals(input)) {
                    return;
                }
                try {
                    client.processInput(input);
                } catch (ProtocolException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Application failed.");
        }
    }

    private void initSocket() throws IOException {
        Socket socket = new Socket(host, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();

        Thread socketListenerThread = new Thread(() -> {
            final byte[] buf = new byte[1024 * 64];
            System.out.println("Starting listener thread...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int read = in.read(buf);
                    if (read > 0) {

                        Message message = protocol.decode(Arrays.copyOf(buf, read));
                        onMessage(message);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to process connection: {}");
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        socketListenerThread.start();
    }

    private void processInput(String line) throws IOException, ProtocolException {
        String[] tokens = line.split(" ");
        System.out.println("Tokens: {}" + Arrays.toString(tokens));
        String cmdType = tokens[0];
        System.out.println(cmdType);
        switch (cmdType) {
            case "/logup":
                LogupMessage registerMessage = new LogupMessage(tokens[1], tokens[2]);
                send(registerMessage);
                break;
            case "/login":
                LoginMessage loginMessage = new LoginMessage(tokens[1], tokens[2]);
                send(loginMessage);
                break;
            case "/chat_create":
                String participantsString = tokens[1];
                List<Long> participants = Arrays.stream(participantsString.split(","))
                        .map(Long::valueOf).collect(Collectors.toList());
                CreateChatMessage createChatMessage = new CreateChatMessage(participants);
                send(createChatMessage);
                break;
            case "/text":
                System.out.println(tokens[1]);
                Long id = Long.parseLong(tokens[1]);
                TextMessage textMessage = new TextMessage(id, tokens[2]);
                send(textMessage);
                break;
            case "/chat_list":
                //TODO
                System.out.println("Not implemented yet");
                break;
            case "/chat_history":
                //TODO
                System.out.println("Not implemented yet");
                break;
            default:
                System.out.println("Invalid input: " + line);
        }
    }

    private void onMessage(Message msg) {
        System.out.println("Message received: {}" + msg);
    }

    private void send(Message message) throws IOException, ProtocolException {
        System.out.println(message.toString());
        out.write(protocol.encode(message));
        out.flush();
    }
}
