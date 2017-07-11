package ru.mail.polis.reimagined_telegram.server.net;

import ru.mail.polis.reimagined_telegram.server.model.User;
import ru.mail.polis.reimagined_telegram.server.model.messages.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class Session {

    private static final int MAX_MSG_SIZE = 1 << 15;
    private static final int TIMEOUT = 100;
    /**
     * Пользователь сессии, пока не прошел логин, user == null
     * После логина устанавливается реальный пользователь
     */
    private User user;
    private Socket socket;
    private Protocol protocol;
    private boolean alive;
    private InputStream in;
    private OutputStream out;

    public Session(Socket clientSocket) {
        try {
            socket = clientSocket;
            socket.setSoTimeout(TIMEOUT);
            System.out.println("Connection: " + socket.getInetAddress() + ", " + this.toString());
            in = socket.getInputStream();
            out = socket.getOutputStream();
            protocol = new StringProtocol();
            alive = true;
        } catch (Exception e) {
            System.out.println(this.getClass() + "Can't create session. " + e.toString());
            close();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() throws IOException, ProtocolException {

        byte[] buf = new byte[MAX_MSG_SIZE];
        int read;
        Message ret = null;
        try {
            read = in.read(buf, 0, MAX_MSG_SIZE);
            if (read > 0) {
                ret = protocol.decode(Arrays.copyOf(buf, read));
            }
        } catch (SocketTimeoutException ste) {
            ret = null;
        }
        return ret;
    }

    public void send(Message message) throws ProtocolException, IOException {
        if (message.getSenderId() != null) {
            System.out.println("sent to: " + message.getSenderId().toString());
        } else {
            System.out.println("sent to: " + null);
        }
        out.write(protocol.encode(message));
        out.flush();
    }

    public void kill() {
        if (!alive) {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void close() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }
}

