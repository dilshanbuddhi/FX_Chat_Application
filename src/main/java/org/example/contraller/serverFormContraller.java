package org.example.contraller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class serverFormContraller {
    public TextArea ctxtArea;
    public TextField cTxtfield;
    public ImageView imageView;

    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String msg = "";

    public void initialize() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(3000);
                Platform.runLater(() -> ctxtArea.appendText("Server started. \n"));
                socket = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (!socket.isClosed()) {
                    String messageType = dataInputStream.readUTF();

                    if (messageType.equals("TEXT")) {
                        ctxtArea.appendText("Client: " + msg + "\n");
                    } else if (messageType.equals("IMAGE")) {
                        int length = dataInputStream.readInt();  // Read the image length
                        byte[] imageBytes = new byte[length];
                        dataInputStream.readFully(imageBytes);  // Read the full image bytes
                        Image image = new Image(new ByteArrayInputStream(imageBytes));
                        Platform.runLater(() -> imageView.setImage(image));
                    } else {
                        Platform.runLater(() -> ctxtArea.appendText("Unknown message type received.\n"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sentOnAction(ActionEvent actionEvent) throws IOException {
        String message = cTxtfield.getText();
        ctxtArea.appendText("you: " + message + "\n");

        dataOutputStream.writeUTF("TEXT");  // Specify that it's a text message
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        cTxtfield.clear();
    }
}
