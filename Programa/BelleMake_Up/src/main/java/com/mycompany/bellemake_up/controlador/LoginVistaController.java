package com.mycompany.bellemake_up.controlador;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class LoginVistaController {

    // Método para abrir la ventana de Iniciar Sesión
    public void abrirInicioSesion(ActionEvent event) {
        System.out.println("Botón Iniciar Sesión presionado"); // Para depurar
        abrirNuevaVista(event, "/fxml/iniciarsesionvista.fxml", "Inicio de sesión - Belle Make Up");
    }

    // Método para abrir la ventana de Registro
    public void abrirRegistro(ActionEvent event) {
     abrirNuevaVista(event, "/fxml/registrarsevista.fxml", "Registro - Belle Make Up");
    }

    // Método genérico para abrir cualquier ventana
    private void abrirNuevaVista(ActionEvent event, String fxmlPath, String title) {
        try {
            // 1. Cerrar la ventana actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // 2. Cargar el FXML de la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // 3. Crear el nuevo Stage y mostrarlo
            Stage nextStage = new Stage();
            nextStage.setTitle(title);
            nextStage.setScene(new Scene(root));
            nextStage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
