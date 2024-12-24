package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)  throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/server_form.fxml"))));
        stage.setTitle("Server Form");
        stage.centerOnScreen();
        stage.show();

        Stage secondstage = new Stage();
        secondstage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/client_form.fxml"))));
        secondstage.setTitle("Client Form");

        secondstage.show();
    }
}
