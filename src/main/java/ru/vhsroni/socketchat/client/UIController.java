package ru.vhsroni.socketchat.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ru.vhsroni.socketchat.network.TCPConnection;

public class UIController {

    @FXML
    private TextField nickName;
    @FXML
    private TextArea log;
    @FXML
    private TextField message;
    @FXML
    private Button sendMessageButton;
    @FXML
    private Button changeNickNameButton;

    private TCPConnection connection;

    public void setConnection(TCPConnection connection) {
       this.connection = connection;
    }

    @FXML
    public void initialize() {
        sendMessageButton.setOnAction(event -> sendMessage());
        changeNickNameButton.setOnAction(event -> changeNickName());
    }

    public void changeNickName() {
        String name = nickName.getText();
        if (name.trim().isEmpty()) return;
        printTextOnLog("Nickname changed to " + nickName.getText());
    }

    public void sendMessage() {
        String msg = nickName.getText() + ": " + message.getText();
        if (msg.trim().isEmpty()) return;
        message.clear();
        connection.sendString(msg);
    }

    protected synchronized void printTextOnLog(String text) {
        log.appendText(text + "\n");
    }
}