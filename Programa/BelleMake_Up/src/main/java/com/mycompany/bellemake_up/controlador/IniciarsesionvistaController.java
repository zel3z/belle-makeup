package com.mycompany.bellemake_up.controlador;

import static com.mycompany.bellemake_up.BelleMake_Up.listaUsuario;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class IniciarsesionvistaController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContra;

    @FXML
    private void iniciarSesion() {

        String usuario = txtUsuario.getText().trim();
        String contra = txtContra.getText().trim();

        if (usuario.isEmpty() || contra.isEmpty()) {
            mostrarAlerta("Error", "Debes llenar todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        boolean loginOK = listaUsuario.realizarLogin(usuario, contra);

        if (loginOK) {
            mostrarAlerta("Bienvenido", "Inicio de sesión exitoso.", Alert.AlertType.INFORMATION);
              // 👉 Después del registro abre el catálogo
            abrirCatalogo();

        } else {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
        }
    }
    
    private void abrirCatalogo() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/catalogovista.fxml"));
        Parent root = loader.load();

        // Crear nueva ventana para el catálogo
        Stage catalogoStage = new Stage();
        catalogoStage.setScene(new Scene(root));
        catalogoStage.setTitle("Catálogo de Belle Make Up");
        catalogoStage.setMaximized(true);
        catalogoStage.show();

        // Ocultar la ventana actual (registro)
        Stage ventanaActual = (Stage) txtUsuario.getScene().getWindow();
        ventanaActual.hide();

    } catch (IOException e) {
        e.printStackTrace();
        mostrarAlerta("Error", "No se pudo abrir el catálogo.", Alert.AlertType.ERROR);
    }
}


    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
