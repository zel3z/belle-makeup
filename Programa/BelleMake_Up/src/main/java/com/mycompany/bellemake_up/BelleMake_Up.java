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
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/previewVista.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Preview");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); 
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/*package com.mycompany.bellemake_up;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BelleMake_Up extends Application {

@Override
public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/vista/previewVista.fxml"));

    Scene scene = new Scene(root);

    // Tamaño base (como lo diseñaste en Scene Builder)
    double baseWidth = 999;
    double baseHeight = 666;

    // Escalar automáticamente
    root.scaleXProperty().bind(scene.widthProperty().divide(baseWidth));
    root.scaleYProperty().bind(scene.heightProperty().divide(baseHeight));

    primaryStage.setScene(scene);
    primaryStage.setTitle("Preview");
    primaryStage.setMaximized(true); // Pantalla completa
    primaryStage.show();
}public static void main(String[] args) {
        launch(args);
    }
}*/