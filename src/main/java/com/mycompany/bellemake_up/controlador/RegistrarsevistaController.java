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

            // ✅ **CORREGIDO**: Configurar usuario nuevo
            configurarUsuarioNuevo(usuario);

            // Abrir el catálogo (vista principal)
            abrirCatalogo();
        }
    }

    // ✅ **MÉTODO CORREGIDO**: Configurar usuario nuevo
    private void configurarUsuarioNuevo(String usuario) {
        try {
            System.out.println("=== REGISTRANDO NUEVO USUARIO: " + usuario + " ===");
            
            // 1. **IMPORTANTE**: Establecer usuario actual en BelleMake_Up
            BelleMake_Up.usuarioActual = usuario;
            System.out.println(" Usuario actual establecido: " + BelleMake_Up.usuarioActual);
            
            // 2. **IMPORTANTE**: Usar la lista GLOBAL de BelleMake_Up (NO crear nueva)
            if (BelleMake_Up.listaProdXusu == null) {
                BelleMake_Up.listaProdXusu = new listaProdXusu();
                System.out.println("Nueva listaProdXusu creada en BelleMake_Up");
            }
            
            // 3. Establecer usuario actual en la lista GLOBAL
            BelleMake_Up.listaProdXusu.setUsuarioActual(usuario);
            System.out.println("Usuario actual en listaProdXusu: " + BelleMake_Up.listaProdXusu.getUsuarioActual());
            
            // 4. **IMPORTANTE**: Cargar datos desde JSON (será vacío para usuario nuevo)
            JsonManager.cargarProdXUsu(BelleMake_Up.listaProdXusu);
            System.out.println("Datos cargados desde prodXusu.json");
            System.out.println("Registros cargados: " + BelleMake_Up.listaProdXusu.tamaño());
            
            // 5. **IMPORTANTE**: Guardar la lista inicial (vacía) para este usuario
            JsonManager.guardarProdXUsu(BelleMake_Up.listaProdXusu);
            System.out.println("Lista guardada en prodXusu.json para usuario nuevo");
            
            // 6. Inicializar sistema de carrito con la lista GLOBAL
            CarritovistaController.inicializarSistema(
                BelleMake_Up.listaProdXusu, 
                usuario, 
                BelleMake_Up.listaInventario
            );
            
            System.out.println("=== USUARIO REGISTRADO EXITOSAMENTE ===");
            System.out.println("Usuario: " + BelleMake_Up.usuarioActual);
            System.out.println("Lista prodXusu inicializada: " + (BelleMake_Up.listaProdXusu != null));
            
        } catch (Exception e) {
            System.err.println("ERROR al configurar usuario nuevo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirCatalogo() {
        try {
            System.out.println("=== ABRIENDO CATÁLOGO DESDE REGISTRO ===");
            System.out.println("Usuario que abre: " + BelleMake_Up.usuarioActual);
            
            // Cargar el FXML del catálogo
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/catalogovista.fxml"));

            // Crear nueva ventana para el catálogo
            Stage catalogoStage = new Stage();
            catalogoStage.setScene(new Scene(root));
            catalogoStage.setTitle("Catálogo de Belle Make Up - " + BelleMake_Up.usuarioActual);
            catalogoStage.setMaximized(true);
            catalogoStage.show();

            // Cerrar ventana actual de registro
            cerrarVentanaActual();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el catálogo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentanaActual() {
        Stage ventanaActual = (Stage) txtUsuario.getScene().getWindow();
        ventanaActual.close();
        System.out.println(" Ventana de registro cerrada");
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}