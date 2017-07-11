package ru.mail.polis.reimagined_telegram.server;

import ru.mail.polis.reimagined_telegram.server.net.Server;


public class Launcher {
    public static void main(String[] args) {
        try {
            new Server(19000, 500).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}