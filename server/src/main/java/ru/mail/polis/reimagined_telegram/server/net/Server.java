package ru.mail.polis.reimagined_telegram.server.net;

import ru.mail.polis.reimagined_telegram.server.commands.MessageCommand;
import ru.mail.polis.reimagined_telegram.server.commands.MessageCommandException;
import ru.mail.polis.reimagined_telegram.server.commands.MessageCommandFactory;
import ru.mail.polis.reimagined_telegram.server.model.messages.Message;
import ru.mail.polis.reimagined_telegram.server.model.messages.Type;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    private ServerSocket serverSocket;
    private BlockingQueue<Session> sessions;

    private int port;
    private int nThreads;
    private boolean alive;
    private DB db = DB.getInstance();

    public Server(int port, int nThreads) {
        this.port = port;
        this.nThreads = nThreads;
    }

    public void start() throws Exception {
        serverSocket = null;
        sessions = new LinkedBlockingQueue<>();
        ExecutorService service = Executors.newFixedThreadPool(nThreads);
        alive = true;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for connection...");
            listen();

            while (alive) {
                Session session = sessions.take();
                Message message = session.getMessage();
                if (message != null) {
                    service.submit(() -> {
                        try {
                            MessageCommand command = MessageCommandFactory.getCommand(message.getType());
                            try {
                                command.execute(session, message, db);
                            } catch (MessageCommandException e) {
                                System.out.println("Error with command: " + e.toString());
                            }
                            sessions.put(session);
                        } catch (InterruptedException e) {
                            System.out.println("Client has disconnected. " + e.toString());
                        }
                    });
                } else if (session.isAlive()) {
                    service.submit(() -> {
                        try {
                            MessageCommand command = MessageCommandFactory.getCommand(Type.MSG_UPD);
                            command.execute(session, message, db);
                            sessions.put(session);
                        } catch (Exception e) {
                            System.out.println(this.getClass() + ": incoming data processing error: " + e.toString());
                        }
                    });
                } else {
                    session.kill();
                }
            }

        } finally {
            sessions.forEach(Session::kill);
            serverSocket.close();
        }
    }

    private void listen() {
        Thread listenerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && alive) {
                Socket clientSocket;
                try {
                    clientSocket = serverSocket.accept();
                    sessions.put(new Session(clientSocket));
                } catch (IOException e) {
                    System.out.println("Client has disconnected. " + e.toString());
                    Thread.currentThread().interrupt();
                } catch (InterruptedException e) {
                    System.out.println("listen: " + e.toString());
                    Thread.currentThread().interrupt();
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

}
