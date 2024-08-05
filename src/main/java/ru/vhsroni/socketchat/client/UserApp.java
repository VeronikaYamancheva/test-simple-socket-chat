package ru.vhsroni.socketchat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.vhsroni.socketchat.network.TCPConnection;
import ru.vhsroni.socketchat.network.TCPConnectionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UserApp extends Application implements TCPConnectionListener{

    private static String IP_ADDR;
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private TCPConnection connection;
    private UIController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("User Chat");
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        InputStream iconStream = this.getClass().getResourceAsStream("images/iconForChat.jpg");
        if (iconStream != null) {
            Image image = new Image(iconStream);
            primaryStage.getIcons().add(image);
        } else {
            System.out.println("failed to load the icon");
        }

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("user-view.fxml"));
        primaryStage.setScene(new Scene((Parent)loader.load()));

        try {
            IP_ADDR = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throwException(connection, e);
        }

        controller = loader.getController();

        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
            controller.setConnection(connection);
        } catch (IOException e) {
            throwException(connection, e);
        }
        primaryStage.show();
    }

    @Override
    public void establishConnection(TCPConnection tcpConnection) {
        controller.printTextOnLog("connection ready " + tcpConnection);
    }

    @Override
    public void receiveMessage(TCPConnection tcpConnection, String value) {
        controller.printTextOnLog(value);
    }

    @Override
    public void brokeConnection(TCPConnection tcpConnection) {
        controller.printTextOnLog("connection close" + tcpConnection);
    }

    @Override
    public void throwException(TCPConnection tcpConnection, Exception e) {
        controller.printTextOnLog(e.toString());
    }

}