package Chatroom;

import javafx.scene.control.TextArea;

import java.util.Scanner;

public class Receiver extends Thread {
    private volatile boolean exit = false;
    private Scanner in;
    private TextArea textArea;

    Receiver(Scanner in, TextArea textArea) {
        this.in = in;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        while (!exit) {
            if (in.hasNextLine()) {
                String input = in.nextLine();
                textArea.appendText(input + "\n");
            }
        }
    }

    void exit() {
        exit = true;
    }
}
