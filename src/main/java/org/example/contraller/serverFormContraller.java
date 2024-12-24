package org.example.contraller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class serverFormContraller {
    public TextArea ctxtArea;
    public TextField cTxtfield;

    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String msg = "";

    public void initialize() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(3000);
                ctxtArea.appendText("Server started. \n");
                socket = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (!socket.isClosed()) {
                    msg = dataInputStream.readUTF();
                    ctxtArea.appendText("Client: " + msg + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sentOnAction(ActionEvent actionEvent) throws IOException {
        String message = cTxtfield.getText();
        ctxtArea.appendText("you: " + message + "\n");

        dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            cTxtfield.clear();

    }
}
