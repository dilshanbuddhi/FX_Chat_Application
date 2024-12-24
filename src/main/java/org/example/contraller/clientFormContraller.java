package org.example.contraller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class clientFormContraller {
    public TextArea clTxtArea;
    public TextField cltextField;

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String msg="";

    public void initialize() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 3000);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                clTxtArea.appendText("Client started. \n");

                while (!socket.isClosed()) {

                    msg = dataInputStream.readUTF();
                    clTxtArea.appendText("Server: " + msg + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sentOnAction(ActionEvent actionEvent) throws IOException {
        String text = cltextField.getText();
            clTxtArea.appendText("you: " + text + "\n");
            dataOutputStream.writeUTF(text);
            dataOutputStream.flush();
            cltextField.clear();
        }
    }

