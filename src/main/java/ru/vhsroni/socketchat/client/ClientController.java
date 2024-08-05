package ru.vhsroni.socketchat.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import ru.vhsroni.socketchat.network.TCPConnection;
import ru.vhsroni.socketchat.network.TCPConnectionListener;

import java.io.IOException;

public class ClientController {

    @FXML
    private TextField nicknameField;
    @FXML
    private TextArea logArea;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;
    @FXML
    private Button setNicknameButton;

    private TCPConnection connection;

    public void setConnection(TCPConnection connection) {
       this.connection = connection;
    }

    @FXML
    public void initialize() {
        sendButton.setOnAction(event -> sendMessage());
        setNicknameButton.setOnAction(event -> setNicknameWithButton());
    }

    public void setNicknameWithButton() {
        String name = nicknameField.getText();
        if (name.trim().isEmpty()) return;
        printTextOnLog("имя пользователя изменено на " + nicknameField.getText());
    }

    public void sendMessage() {
        System.out.println("внутри условия");
        String msg = nicknameField.getText() + ": " + messageField.getText();
        if (msg.trim().isEmpty()) return;
        messageField.clear();
        connection.sendString(msg);
    }


    protected synchronized void printTextOnLog(String text) {
        logArea.appendText(text + "\n");
    }
}