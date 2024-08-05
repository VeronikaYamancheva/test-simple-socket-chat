package ru.vhsroni.socketchat.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection {

    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final TCPConnectionListener eventListener;

    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port) throws IOException{
        this(new Socket(ipAddr, port), eventListener);
    }

    public TCPConnection(Socket socket, TCPConnectionListener eventListener) throws IOException {
        this.socket = socket;
        this.eventListener = eventListener;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),  StandardCharsets.UTF_8));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.establishConnection(TCPConnection.this);
                    while (!rxThread.isInterrupted()) {
                        eventListener.receiveMessage(TCPConnection.this, in.readLine());
                    }
                }
                catch (IOException e) {
                    eventListener.throwException(TCPConnection.this, e);
                }
                finally {
                    eventListener.brokeConnection(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }

    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.throwException(TCPConnection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.throwException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection:" +
                " IP = " + socket.getInetAddress() +
                " Port = " + socket.getPort();
    }
}
