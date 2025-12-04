package com.mycompany.bellemake_up.controlador;

import com.mycompany.bellemake_up.BelleMake_Up;
import com.mycompany.bellemake_up.modelo.producto;
import com.mycompany.bellemake_up.modelo.prodXusu;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class FavoritosvistaController implements Initializable {

    @FXML private VBox contenedorFavoritos;
    @FXML private Label lblFavoritosVacios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inicializando FavoritosvistaController...");
        cargarFavoritos();
    }
    
    private void cargarFavoritos() {
        contenedorFavoritos.getChildren().clear();
        
        if (BelleMake_Up.usuarioActual == null || BelleMake_Up.listaProdXusu == null) {
            lblFavoritosVacios.setText("Inicia sesión para ver tus favoritos");
            lblFavoritosVacios.setVisible(true);
            return;
        }
        
        // Obtener productos favoritos del usuario
        java.util.ArrayList<prodXusu> favoritos = BelleMake_Up.listaProdXusu.obtenerFavoritos();
        
        if (favoritos.isEmpty()) {
            lblFavoritosVacios.setVisible(true);
            return;
        }
        
        lblFavoritosVacios.setVisible(false);
        
        for (prodXusu pxu : favoritos) {
            // Buscar el producto completo por ID
            producto prod = buscarProductoPorId(pxu.getIdprod());
            if (prod != null) {
                HBox itemFavorito = crearItemFavorito(prod);
                contenedorFavoritos.getChildren().add(itemFavorito);
            }
        }
        
        System.out.println("Favoritos cargados: " + favoritos.size() + " productos");
    }
    
    // Buscar producto por ID en el inventario
    private producto buscarProductoPorId(String idprod) {
        if (BelleMake_Up.listaInventario == null || BelleMake_Up.listaInventario.cab == null) {
            return null;
        }
        
        com.mycompany.bellemake_up.modelo.nodo<producto> actual = BelleMake_Up.listaInventario.cab;
        while (actual != null) {
            if (actual.info.getIdprod().equals(idprod)) {
                return actual.info;
            }
            actual = actual.sig;
        }
        return null;
    }
    
    private HBox crearItemFavorito(producto p) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: #e0e0e0;");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefHeight(100);

        // Imagen del producto
        ImageView img = new ImageView();
        img.setFitWidth(70);
        img.setFitHeight(70);
        img.setPreserveRatio(true);
        
        try {
            String rutaImagenRaw = p.getRutaImagen();
            String nombreArchivo = rutaImagenRaw.replace("@../image/", "");
            String rutaImagen = "/image/" + nombreArchivo;
            URL imagenUrl = getClass().getResource(rutaImagen);
            
            if (imagenUrl != null) {
                Image image = new Image(imagenUrl.toExternalForm());
                img.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen del favorito: " + e.getMessage());
        }

        // Información del producto
        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(300);
        
        Label nombre = new Label(p.getNomprod());
        nombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-wrap-text: true;");
        
        Label precio = new Label("$" + p.getPrecio());
        precio.setStyle("-fx-font-size: 16px; -fx-text-fill: #E91E63; -fx-font-weight: bold;");
        
        Label categoria = new Label(p.getCategoria());
        categoria.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        
        infoBox.getChildren().addAll(nombre, precio, categoria);

        // Botones de acción
        HBox botonesBox = new HBox(10);
        botonesBox.setAlignment(Pos.CENTER);
        
        // Botón agregar al carrito
        Button btnCarrito = new Button("Agregar al Carrito");
        btnCarrito.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 120;");
        btnCarrito.setOnAction(e -> agregarAlCarritoDesdeFavoritos(p));
        
        // Botón eliminar de favoritos
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 80;");
        btnEliminar.setOnAction(e -> eliminarDeFavoritos(p));

        botonesBox.getChildren().addAll(btnCarrito, btnEliminar);

        // Organizar en el card
        card.getChildren().addAll(img, infoBox, botonesBox);
        
        return card;
    }
    
    private void agregarAlCarritoDesdeFavoritos(producto p) {
        if (p != null) {
            // Agregar 1 unidad al carrito
            CarritovistaController.agregarAlCarrito(p, 1);
            
            // Guardar en prodXusu si hay usuario logeado
            if (BelleMake_Up.usuarioActual != null && BelleMake_Up.listaProdXusu != null) {
                try {
                    BelleMake_Up.listaProdXusu.agregarOActualizarCarritoConCantidad(
                        BelleMake_Up.usuarioActual, 
                        p.getIdprod(), 
                        1
                    );
                    
                    JsonManager.guardarProdXUsu(BelleMake_Up.listaProdXusu);
                    
                } catch (Exception e) {
                    System.err.println("Error al guardar en carrito: " + e.getMessage());
                }
            }
            
            String mensaje = "✅ " + p.getNomprod() + " agregado al carrito";
            System.out.println(mensaje);
            mostrarAlerta("Carrito", mensaje);
        }
    }
    
    private void eliminarDeFavoritos(producto p) {
        if (p != null && BelleMake_Up.usuarioActual != null && BelleMake_Up.listaProdXusu != null) {
            try {
                // Buscar el nodo y eliminar de favoritos
                com.mycompany.bellemake_up.modelo.nodo<prodXusu> nodo = 
                    BelleMake_Up.listaProdXusu.buscarNodo(BelleMake_Up.usuarioActual, p.getIdprod());
                
                if (nodo != null) {
                    nodo.info.setFavorito(false);
                    
                    // Guardar cambios en JSON
                    JsonManager.guardarProdXUsu(BelleMake_Up.listaProdXusu);
                    
                    // Recargar la vista
                    cargarFavoritos();
                    
                    String mensaje = "" + p.getNomprod() + " eliminado de favoritos";
                    System.out.println(mensaje);
                    mostrarAlerta("Favoritos", mensaje);
                }
                
            } catch (Exception e) {
                System.err.println("Error al eliminar de favoritos: " + e.getMessage());
                mostrarAlerta("Error", "No se pudo eliminar de favoritos");
            }
        }
    }
    
    @FXML
    private void volverAlCatalogo() {
        try {
            Stage stage = (Stage) contenedorFavoritos.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/catalogovista.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("Volviendo al catálogo...");
        } catch (IOException e) {
            System.err.println("Error al volver al catálogo: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo volver al catálogo: " + e.getMessage());
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}