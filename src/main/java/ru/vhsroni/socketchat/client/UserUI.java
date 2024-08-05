package ru.vhsroni.socketchat.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.vhsroni.socketchat.network.TCPConnection;
import ru.vhsroni.socketchat.network.TCPConnectionListener;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class UserUI extends Application implements TCPConnectionListener{

    private static final String IP_ADDR = "192.168.1.43";
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private TCPConnection connection;

    private ClientController controller;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Chat Client");

        InputStream iconStream = this.getClass().getResourceAsStream("images/iconForChat.jpg");
        if (iconStream != null) {
            Image image = new Image(iconStream);
            primaryStage.getIcons().add(image);
        } else {
            System.out.println("Не удалось загрузить иконку.");
        }

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("user-view.fxml"));
        primaryStage.setScene(new Scene((Parent)loader.load()));


        controller = loader.getController();

        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
            controller.setConnection(connection);
        } catch (IOException e) {
            onException(connection, e);
        }
        primaryStage.show();


    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        controller.printTextOnLog("connection ready " + tcpConnection);
        System.out.println("OnConnectionReady контроллер");

    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        controller.printTextOnLog(value);
        System.out.println("OnReceivingMessage контроллер");

    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        controller.printTextOnLog("connection close" + tcpConnection);
        System.out.println("OnDisconnect контроллер");

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        controller.printTextOnLog(e.toString());
        System.out.println("OnException контроллер");

    }

}