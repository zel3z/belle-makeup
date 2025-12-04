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
    
    // USUARIO ADMINISTRADOR PREDETERMINADO
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String contra = txtContra.getText().trim();

        if (usuario.isEmpty() || contra.isEmpty()) {
            mostrarAlerta("Error", "Debes llenar todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        // Verificar si es el administrador predeterminado
        if (usuario.equals(ADMIN_USER) && contra.equals(ADMIN_PASS)) {
            // Login como administrador exitoso
            mostrarAlerta("Bienvenido Administrador", "Acceso al panel de administración.", Alert.AlertType.INFORMATION);
            
            // Configurar usuario admin
            configurarUsuarioLogin(usuario);
            
            // Abrir vista de administrador
            abrirVistaAdministrador();
            
        } else {
            // Verificar login normal de cliente
            boolean loginOK = BelleMake_Up.listaUsuario.realizarLogin(usuario, contra);

            if (loginOK) {
                mostrarAlerta("Bienvenido", "Inicio de sesión exitoso.", Alert.AlertType.INFORMATION);
                
                // Configurar usuario después del login
                configurarUsuarioLogin(usuario);
                
                // Abrir el catálogo (vista principal para clientes)
                abrirCatalogo();

            } else {
                mostrarAlerta("Error", "Usuario o contraseña incorrectos.", Alert.AlertType.ERROR);
            }
        }
    }
    
    // Método para abrir vista de administrador
    private void abrirVistaAdministrador() {
        try {
            System.out.println("=== ABRIENDO PANEL DE ADMINISTRACIÓN ===");
            System.out.println("Usuario Admin: " + BelleMake_Up.usuarioActual);
            
            // Cargar el FXML de administrador
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/adminvista.fxml"));

            Stage adminStage = new Stage();
            adminStage.setScene(new Scene(root));
            adminStage.setTitle("Panel de Administración - Balle Make Up");
            adminStage.setMaximized(true);
            adminStage.show();

            // Cerrar ventana actual de login
            cerrarVentanaActual();
            
            System.out.println("Panel de administración abierto exitosamente");
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el panel de administración: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    // ✅ **MÉTODO CORREGIDO**: Configurar usuario después del login
    private void configurarUsuarioLogin(String usuario) {
        try {
            System.out.println("=== CONFIGURANDO USUARIO LOGIN: " + usuario + " ===");
            
            // 1. Establecer usuario actual
            BelleMake_Up.usuarioActual = usuario;
            System.out.println("Usuario actual establecido: " + BelleMake_Up.usuarioActual);
            
            // 2. Usar la lista GLOBAL (NO crear nueva)
            if (BelleMake_Up.listaProdXusu == null) {
                BelleMake_Up.listaProdXusu = new listaProdXusu();
                System.out.println("Nueva listaProdXusu creada en BelleMake_Up");
            }
            
            // 3. Establecer usuario actual en la lista GLOBAL
            BelleMake_Up.listaProdXusu.setUsuarioActual(usuario);
            System.out.println("Usuario actual en listaProdXusu: " + BelleMake_Up.listaProdXusu.getUsuarioActual());
            
            // 4. Cargar datos DESDE JSON para este usuario (solo si no es admin)
            if (!usuario.equals(ADMIN_USER)) {
                JsonManager.cargarProdXUsu(BelleMake_Up.listaProdXusu);
                System.out.println("Datos cargados desde prodXusu.json para usuario: " + usuario);
                System.out.println("Registros cargados: " + BelleMake_Up.listaProdXusu.tamaño());
                
                // 5. Inicializar carrito con la lista GLOBAL (solo para clientes)
                CarritovistaController.inicializarSistema(
                    BelleMake_Up.listaProdXusu, 
                    usuario, 
                    BelleMake_Up.listaInventario
                );
            }
            
            System.out.println("=== USUARIO CONFIGURADO EXITOSAMENTE ===");
            
        } catch (Exception e) {
            System.err.println("ERROR al configurar usuario login: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void abrirCatalogo() {
        try {
            System.out.println("=== ABRIENDO CATÁLOGO DESDE LOGIN ===");
            System.out.println("Usuario: " + BelleMake_Up.usuarioActual);
            
            // Cargar el FXML del catálogo
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/catalogovista.fxml"));

            Stage catalogoStage = new Stage();
            catalogoStage.setScene(new Scene(root));
            catalogoStage.setTitle("Catálogo de Belle Make Up - " + BelleMake_Up.usuarioActual);
            catalogoStage.setMaximized(true);
            catalogoStage.show();

            // Cerrar ventana actual de login
            cerrarVentanaActual();
            
            System.out.println("Catálogo abierto exitosamente");
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir el catálogo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentanaActual() {
        Stage ventanaActual = (Stage) txtUsuario.getScene().getWindow();
        ventanaActual.close();
        System.out.println("Ventana de login cerrada");
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}