package ru.vhsroni.socketchat.client;

import ru.vhsroni.socketchat.network.TCPConnection;
import ru.vhsroni.socketchat.network.TCPConnectionListener;

import java.awt.*;
import java.io.IOException;

public class ClientConnection implements TCPConnectionListener {

    private final ClientController clientController;

    public ClientConnection(ClientController clientController) {
        this.clientController = clientController;
    }


    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        clientController.printTextOnLog("connection ready " + tcpConnection);
        System.out.println("OnConnectionReady контроллер");

    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        clientController.printTextOnLog(value);
        System.out.println("OnReceivingMessage контроллер");

    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        clientController.printTextOnLog("connection close" + tcpConnection);
        System.out.println("OnDisconnect контроллер");

    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        clientController.printTextOnLog(e.toString());
        System.out.println("OnException контроллер");

    }
}
