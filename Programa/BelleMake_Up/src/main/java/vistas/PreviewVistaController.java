package vistas;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PreviewVistaController {

    // ... otros métodos e inicializaciones ...

    public void handleVerCatalogo(ActionEvent event) {
        try {
           
            // 1. Cargar el archivo CatalogoVista.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/catalogovista.fxml"));
            Parent root = loader.load();
            
            // 2. Crear una nueva escena con el contenido cargado
            Scene scene = new Scene(root);
            
            // 3. Crear una nueva ventana (Stage)
            Stage newStage = new Stage();
            newStage.setTitle("Catálogo de Productos - Belle Make Up");
            newStage.setScene(scene);
            
            // 4. Establecer la ventana en modo pantalla completa
            newStage.setMaximized(true); 
            
            // 5. Mostrar la nueva ventana
            newStage.show();
            Stage ventanaActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
                ventanaActual.hide();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista del Catálogo.");
            e.printStackTrace();
        }
    }
    
    
  
    public void handleAbrirLogin(ActionEvent event) {
        try {
            // 1. Cargar el FXML de la vista de login
            // Asegúrate de que la ruta sea correcta (ej: /vistas/loginVista.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginVista.fxml"));
            Parent root = loader.load();
            
            // 2. Crear una nueva ventana (Stage)
            Stage loginStage = new Stage();
            
            // 3. Configurar como modal (opcional, pero recomendado para diálogos de login)
            // Bloquea la interacción con la ventana principal hasta que se cierre.
            loginStage.initModality(Modality.APPLICATION_MODAL); 
            
            loginStage.setTitle("Iniciar Sesión / Registrarse");
            loginStage.setScene(new Scene(root));
            
            // 4. Mostrar el diálogo
            loginStage.showAndWait(); 
            
        } catch (IOException e) {
            System.err.println("Error al cargar la vista de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}