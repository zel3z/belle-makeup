package com.mycompany.bellemake_up.controlador;

import com.mycompany.bellemake_up.BelleMake_Up;
import com.mycompany.bellemake_up.modelo.listaProdXusu;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

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
        String nombre = txtNombre.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String contra = txtContra.getText().trim();
        String correo = txtCorreo.getText().trim();

        // Validar campos vacíos
        if (nombre.isEmpty() || usuario.isEmpty() || contra.isEmpty() || correo.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        // Rol por defecto (siempre cliente)
        String rol = "cliente";

        boolean registrado = BelleMake_Up.listaUsuario.registrarUsuario(
                nombre,
                usuario,
                contra,
                correo,
                rol
        );

        if (!registrado) {
            mostrarAlerta("Error", "El usuario ya existe.", Alert.AlertType.ERROR);
        } else {
            mostrarAlerta("Éxito", "Usuario registrado correctamente.", Alert.AlertType.INFORMATION);

            //  INICIALIZAR SISTEMA DE CARRITO POR USUARIO
            inicializarSistemaCarrito(usuario);

            //  Después del registro abre el catálogo
            abrirCatalogo();
        }
    }

    //  MÉTODO NUEVO: Inicializar sistema de carrito por usuario
    private void inicializarSistemaCarrito(String usuario) {
        try {
            // Crear lista prodXusu para el nuevo usuario
            listaProdXusu listaProdXUsu = new listaProdXusu();
            listaProdXUsu.setUsuarioActual(usuario);

            // Para un usuario nuevo, no hay datos previos, pero inicializamos igual
            JsonManager.cargarProdXUsu(listaProdXUsu);

            //  Usar el inventario estático de la clase principal
            CarritovistaController.inicializarSistema(listaProdXUsu, usuario, BelleMake_Up.listaInventario);

            //  Guardar el usuario actual en la clase principal
            BelleMake_Up.usuarioActual = usuario;
            BelleMake_Up.listaProdXusu = listaProdXUsu;

            System.out.println(" Sistema de carrito inicializado para nuevo usuario: " + usuario);

        } catch (Exception e) {
            System.err.println("Error al inicializar sistema de carrito: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirCatalogo() {
        try {
            // ✅ Cargar el FXML del catálogo
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/catalogovista.fxml"));

            // Crear nueva ventana para el catálogo
            Stage catalogoStage = new Stage();
            catalogoStage.setScene(new Scene(root));
            catalogoStage.setTitle("Catálogo de Belle Make Up");
            catalogoStage.setMaximized(true);
            catalogoStage.show();

            // ✅ CERRAR TODAS LAS VENTANAS ANTERIORES
            cerrarTodasLasVentanasAnteriores();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el catálogo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // ✅ MÉTODO MEJORADO: Cerrar todas las ventanas anteriores
    private void cerrarTodasLasVentanasAnteriores() {
        // Cerrar la ventana actual de registro
        Stage ventanaActual = (Stage) txtUsuario.getScene().getWindow();
        ventanaActual.close();

        System.out.println(" Ventanas anteriores cerradas");
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}
