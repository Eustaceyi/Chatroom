package Chatroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Controller {
    @FXML
    private TextArea textArea;
    @FXML
    private Button sendButton;
    @FXML
    private TextField inputField;
    @FXML
    private Button quitButton;

    private String ipAddr;
    private int socketNum;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Receiver r;

    @FXML
    public void initialize() throws IOException {
        sendButton.setDisable(true);
        textArea.setEditable(false);
        ipAddr = "35.236.240.248";
        socketNum = 12000;
        socket = new Socket(ipAddr, socketNum);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        r = new Receiver(in, textArea);
        r.start();
    }

    @FXML
    public void onButtonClicked(ActionEvent e) {
        String text = inputField.getText();
        out.println(text);
        inputField.clear();
        sendButton.setDisable(true);
    }

    @FXML
    public void onQuitClicked(ActionEvent e) {
        out.println("/quit");
        r.exit();
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleKeyReleased(){
        String text = inputField.getText();
        boolean disableButtons = text.isEmpty() || text.trim().isEmpty();
        sendButton.setDisable(disableButtons);
    }
}
