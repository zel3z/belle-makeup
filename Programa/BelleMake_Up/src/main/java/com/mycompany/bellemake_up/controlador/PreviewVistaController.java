package com.mycompany.bellemake_up.controlador;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PreviewVistaController {
    
  
    public void handleAbrirLogin(ActionEvent event) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginVista.fxml"));
            Parent root = loader.load();
            
            
            Stage loginStage = new Stage();
            
            
            loginStage.initModality(Modality.APPLICATION_MODAL); 
            
            loginStage.setTitle("Iniciar Sesión / Registrarse");
            loginStage.setScene(new Scene(root));
            
           
            loginStage.showAndWait(); 
            
        } catch (IOException e) {
            System.err.println("Error al cargar la vista de Login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}