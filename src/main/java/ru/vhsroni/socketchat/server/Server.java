package ru.vhsroni.socketchat.server;

import ru.vhsroni.socketchat.network.TCPConnection;
import ru.vhsroni.socketchat.network.TCPConnectionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server implements TCPConnectionListener {

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    public static void main(String[] args) {
        new Server();
    }

    private Server() {
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
    public synchronized void establishConnection(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("client connected: " + tcpConnection);
    }

    @Override
    public synchronized void receiveMessage(TCPConnection tcpConnection, String value) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        sendToAllConnections("<" + time + "> " + value);
    }

    @Override
    public synchronized void brokeConnection(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void throwException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection Exception: " + e);
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        for (TCPConnection connection : connections) connection.sendString(value);
    }
}

