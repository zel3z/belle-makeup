package com.mycompany.bellemake_up;

import com.mycompany.bellemake_up.controlador.JsonManager;
import com.mycompany.bellemake_up.modelo.listaInventario;
import com.mycompany.bellemake_up.modelo.listaProdXusu;
import com.mycompany.bellemake_up.modelo.listaUsuario;
import com.mycompany.bellemake_up.modelo.producto;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class BelleMake_Up extends Application {

    // Variables globales del sistema
    public static listaInventario listaInventario = new listaInventario();
    public static listaUsuario listaUsuario = new listaUsuario();
    public static listaProdXusu listaProdXusu = new listaProdXusu();
    public static String usuarioActual = null;
    public static producto compraDirectaProducto = null;
    public static int compraDirectaCantidad = 0;
    @Override
    public void start(Stage primaryStage) {
        try {
            inicializarSistema();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/previewVista.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Belle Makeup");
            primaryStage.setScene(scene);
            primaryStage.show();
            configurarCierrePrograma(primaryStage);
            primaryStage.setMaximized(true);

        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación:");
            e.printStackTrace();
        }
    }

    private void inicializarSistema() {
        System.out.println("Inicializando sistema...");
        JsonManager.inicializarArchivos();
        JsonManager.cargarInventario(listaInventario);
        JsonManager.cargarUsuarios(listaUsuario);
        System.out.println("Sistema inicializado.");
    }

    private void configurarCierrePrograma(Stage stage) {
        stage.setOnCloseRequest(event -> {
            System.out.println("Guardando datos...");
            JsonManager.guardarTodo(listaInventario, listaUsuario, listaProdXusu);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}