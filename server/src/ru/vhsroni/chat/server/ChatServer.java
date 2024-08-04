package ru.vhsroni.chat.server;

import ru.vhsroni.network.TCPConnection;
import ru.vhsroni.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    public static void main(String[] args) {
        new ChatServer();
    }

    private ChatServer() {
        System.out.println("server running...");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while(true) {
                try {
                    new TCPConnection(serverSocket.accept(), this);
                } catch (IOException e) {
                    System.out.println("TCPConnection Exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendAllConnections("client connected: " + tcpConnection);
    }

    @Override
    public  synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendAllConnections("client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection Exception: " + e);
    }

    private void sendAllConnections(String value) {
        System.out.println(value);
        for (TCPConnection connection : connections) connection.sendString(value);
    }
}
