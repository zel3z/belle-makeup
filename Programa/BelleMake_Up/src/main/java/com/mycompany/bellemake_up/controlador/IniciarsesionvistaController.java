package com.mycompany.bellemake_up.controlador;

import com.mycompany.bellemake_up.BelleMake_Up;
import com.mycompany.bellemake_up.modelo.listaProdXusu;
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

        boolean loginOK = BelleMake_Up.listaUsuario.realizarLogin(usuario, contra);

        if (loginOK) {
            mostrarAlerta("Bienvenido", "Inicio de sesión exitoso.", Alert.AlertType.INFORMATION);
            
            // ✅ INICIALIZAR SISTEMA DE CARRITO POR USUARIO
            inicializarSistemaCarrito(usuario);
            
            // 👉 Después del login abre el catálogo
            abrirCatalogo();

        } else {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
        }
    }
    
    //  MÉTODO NUEVO: Inicializar sistema de carrito por usuario
    private void inicializarSistemaCarrito(String usuario) {
        try {
            // Crear lista prodXusu para el usuario
            listaProdXusu listaProdXUsu = new listaProdXusu();
            listaProdXUsu.setUsuarioActual(usuario);
            
            // Cargar datos del usuario desde JSON
            JsonManager.cargarProdXUsu(listaProdXUsu);
            
            // ✅ Usar el inventario estático de la clase principal
            CarritovistaController.inicializarSistema(listaProdXUsu, usuario, BelleMake_Up.listaInventario);
            
            // ✅ Guardar el usuario actual en la clase principal
            BelleMake_Up.usuarioActual = usuario;
            BelleMake_Up.listaProdXusu = listaProdXUsu;
            
            System.out.println("Sistema de carrito inicializado para: " + usuario);
            
        } catch (Exception e) {
            System.err.println(" Error al inicializar sistema de carrito: " + e.getMessage());
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
        // Cerrar la ventana actual de login
        Stage ventanaActual = (Stage) txtUsuario.getScene().getWindow();
        ventanaActual.close();
        
        System.out.println(" Ventanas anteriores cerradas");
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}