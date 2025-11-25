package com.mycompany.bellemake_up.controlador;

//import com.mycompany.bellemake_up.json.JsonManager;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.belle_makeup.modelo.listaUsuario;

public class RegistrarsevistaController {

    @FXML
    private TextField txtNombre;
    
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContra;

    @FXML
    private TextField txtCorreo;

    @FXML
    private void registrar() {
        String nombre= txtNombre.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String contra = txtContra.getText().trim();
        String correo = txtCorreo.getText().trim();

        // Validar campos vacíos
        if (usuario.isEmpty() || contra.isEmpty() || correo.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        // Rol por defecto (siempre cliente)
        String rol = "cliente";

        boolean registrado = listaUsuario.registrarUsuario(
                nombre,
                usuario,   // si txtTelefono es el "nombre", ajusta aquí
                contra,
                correo, // si no tienes campo de correo
                rol
        );

        if (!registrado) {
            mostrarAlerta("Error", "El usuario ya existe.", Alert.AlertType.ERROR);
        } else {
            mostrarAlerta("Éxito", "Usuario registrado correctamente.", Alert.AlertType.INFORMATION);
              // 👉 Después del registro abre el catálogo
            abrirCatalogo();
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
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}
