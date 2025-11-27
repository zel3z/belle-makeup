package com.mycompany.bellemake_up.controlador;

import com.mycompany.bellemake_up.modelo.listaInventario;
import com.mycompany.bellemake_up.modelo.nodo;
import com.mycompany.bellemake_up.modelo.producto;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CatalogovistaController implements Initializable {

    private listaInventario inventario = new listaInventario();
    @FXML
    private VBox contenedorProductos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inicializando CatalogovistaController...");
        System.out.println("contenedorProductos es null: " + (contenedorProductos == null));
        
        if (contenedorProductos == null) {
            System.err.println("ERROR: contenedorProductos no fue inyectado desde FXML");
            return;
        }
        
        JsonManager.cargarInventario(inventario);
        mostrarProductos();
    }

    private void mostrarProductos() {
        // Mantener solo el banner y los filtros (primeros 2 elementos)
        // Eliminar todos los productos estáticos que están después del índice 2
        if (contenedorProductos.getChildren().size() > 2) {
            contenedorProductos.getChildren().remove(2, contenedorProductos.getChildren().size());
        }
        
        nodo<producto> actual = inventario.cab;
        int contador = 0;

        while (actual != null) {
            producto p = actual.info;
            HBox item = crearTarjetaProducto(p);
            contenedorProductos.getChildren().add(item);
            actual = actual.sig;
            contador++;
        }
        
        System.out.println("Productos dinámicos mostrados: " + contador);
    }

    private HBox crearTarjetaProducto(producto p) {
        HBox tarjeta = new HBox();
        tarjeta.setSpacing(15);
        tarjeta.setStyle("-fx-padding: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #f9f9f9;");
        tarjeta.setPrefWidth(300);

        // Imagen
        ImageView img = new ImageView();
        img.setFitWidth(120);
        img.setFitHeight(120);
        img.setPreserveRatio(true);

        try {
            String rutaImagen = "/com/mycompany/bellemake_up/image/" + p.getRutaImagen();
            URL imagenUrl = getClass().getResource(rutaImagen);
            
            if (imagenUrl != null) {
                Image image = new Image(imagenUrl.toExternalForm());
                img.setImage(image);
            } else {
                System.err.println("No se encontró la imagen: " + rutaImagen);
                // Intentar cargar imagen por defecto
                URL defaultImage = getClass().getResource("/com/mycompany/bellemake_up/imagenes/default.png");
                if (defaultImage != null) {
                    img.setImage(new Image(defaultImage.toExternalForm()));
                }
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen de: " + p.getNomprod());
            e.printStackTrace();
        }

        // Información del producto
        VBox infoBox = new VBox(8);
        infoBox.setPrefWidth(150);
        
        Label nombre = new Label(p.getNomprod());
        nombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-wrap-text: true;");
        
        Label precio = new Label("$" + p.getPrecio());
        precio.setStyle("-fx-font-size: 16px; -fx-text-fill: #e91e63; -fx-font-weight: bold;");
        
        Label categoria = new Label(p.getCategoria());
        categoria.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        
        infoBox.getChildren().addAll(nombre, precio, categoria);
        tarjeta.getChildren().addAll(img, infoBox);

        // Efecto hover
        tarjeta.setOnMouseEntered(e -> {
            tarjeta.setStyle("-fx-padding: 15; -fx-border-color: #ffb6c1; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #fff0f3; -fx-cursor: hand;");
        });
        
        tarjeta.setOnMouseExited(e -> {
            tarjeta.setStyle("-fx-padding: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #f9f9f9;");
        });

        // CLICK → abrir detalles
        tarjeta.setOnMouseClicked(evt -> abrirDetalles(p));

        return tarjeta;
    }

private void abrirDetalles(producto p) {
    try {
        System.out.println("=== DEBUG COMPLETO: ABRIENDO DETALLES ===");
        System.out.println("📦 Producto: " + p.getNomprod());
        
        // Verificar que el FXML existe
        String ruta = "/fxml/vistaDetallesProducto.fxml";
        System.out.println("🔍 Buscando FXML en: " + ruta);
        
        URL fxmlUrl = getClass().getResource(ruta);
        System.out.println("📍 URL encontrada: " + fxmlUrl);
        
        if (fxmlUrl == null) {
            System.err.println("❌ ERROR: El FXML no se encuentra en: " + ruta);
            System.err.println("📂 Verifica que el archivo esté en: src/main/resources/fxml/vistaDetallesProducto.fxml");
            
            // Listar qué archivos sí existen en /fxml/
            System.err.println("📁 Archivos en /fxml/:");
            try {
                java.nio.file.Path path = java.nio.file.Paths.get("src/main/resources/fxml/");
                if (java.nio.file.Files.exists(path)) {
                    java.nio.file.Files.list(path).forEach(file -> 
                        System.err.println("   - " + file.getFileName()));
                }
            } catch (Exception e) {
                System.err.println("   No se pudo listar archivos");
            }
            return;
        }
        
        System.out.println("✅ FXML encontrado, procediendo a cargar...");
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        System.out.println("✅ FXML cargado exitosamente");

        // Obtener el controlador
        VistaDetallesProductoController controller = loader.getController();
        System.out.println("🎮 Controlador obtenido: " + (controller != null));
        
        if (controller == null) {
            System.err.println("❌ ERROR: No se pudo obtener el controlador");
            System.err.println("📋 Verifica que en vistaDetallesProducto.fxml tengas:");
            System.err.println("   fx:controller=\"com.mycompany.bellemake_up.controlador.VistaDetallesProductoController\"");
            return;
        }
        
        controller.setProducto(p);
        System.out.println("✅ Producto pasado al controlador: " + p.getNomprod());

        // Crear y mostrar la ventana
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Detalles - " + p.getNomprod());
        stage.show();
        
        System.out.println("🎉 VENTANA DE DETALLES ABIERTA EXITOSAMENTE");
        System.out.println("=============================================");

    } catch (Exception e) {
        System.err.println("💥 ERROR CRÍTICO al abrir detalles:");
        e.printStackTrace();
        
        // Mostrar mensaje de error al usuario
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No se pudo abrir los detalles del producto");
        alert.setContentText("Error: " + e.getMessage());
        alert.showAndWait();
    }
}

    @FXML
    private void volverPreview(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/previewvista.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setMaximized(true); 
    }
}
