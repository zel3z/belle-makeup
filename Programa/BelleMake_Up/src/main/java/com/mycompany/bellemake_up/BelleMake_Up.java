package com.mycompany.bellemake_up;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BelleMake_Up extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar la vista desde resources/vistas/Preview.fxml
        Parent root = FXMLLoader.load(getClass().getResource("/vista/previewVista.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Preview");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}