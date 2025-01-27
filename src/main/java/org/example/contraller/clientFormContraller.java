package org.example.contraller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class clientFormContraller {
    public TextArea clTxtArea;
    public TextField cltextField;

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String msg = "";

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

    // Sending a text message
    public void sentOnAction(ActionEvent actionEvent) throws IOException {
        String text = cltextField.getText();
        clTxtArea.appendText("You: " + text + "\n");

        // Specify the message type as "TEXT"
        dataOutputStream.writeUTF("TEXT");
        dataOutputStream.writeUTF(text);
        dataOutputStream.flush();
        cltextField.clear();
    }

    // Select and send image
    public void selectAndSendImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        // Show the dialog and get the selected file
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            clTxtArea.appendText("Selected image: " + selectedFile.getName() + "\n");
            try {
                sendImage(selectedFile); // Send the selected image
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Send image file method
    public void sendImage(File imageFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(imageFile);
        byte[] imageBytes = new byte[(int) imageFile.length()];
        fileInputStream.read(imageBytes);
        fileInputStream.close();

        // Specify the message type as "IMAGE"
        dataOutputStream.writeUTF("IMAGE");
        dataOutputStream.writeInt(imageBytes.length); // Send the length of the image data
        dataOutputStream.write(imageBytes);           // Send the image bytes
        dataOutputStream.flush();
        clTxtArea.appendText("Image sent!\n");
    }
}
