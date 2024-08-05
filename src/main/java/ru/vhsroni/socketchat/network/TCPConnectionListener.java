package ru.vhsroni.socketchat.network;

public interface TCPConnectionListener {

    void establishConnection(TCPConnection tcpConnection);
    void receiveMessage(TCPConnection tcpConnection, String value);
    void brokeConnection(TCPConnection tcpConnection);
    void throwException(TCPConnection tcpConnection, Exception e);
}

