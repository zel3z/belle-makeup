package com.mycompany.bellemake_up.controlador;

import com.mycompany.bellemake_up.BelleMake_Up;
import com.mycompany.bellemake_up.modelo.producto;
import com.mycompany.bellemake_up.modelo.prodXusu;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;

public class HistorialComprasvistaController implements Initializable {

    @FXML private VBox contenedorCompras;
    @FXML private Label lblSinCompras;
    
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(" Inicializando HistorialComprasController...");
        cargarHistorialCompras();
    }
    
    private void cargarHistorialCompras() {
        contenedorCompras.getChildren().clear();
        
        if (BelleMake_Up.usuarioActual == null || BelleMake_Up.listaProdXusu == null) {
            lblSinCompras.setText("Inicia sesión para ver tu historial");
            lblSinCompras.setVisible(true);
            return;
        }
        
        // Obtener compras del usuario
        ArrayList<prodXusu> compras = BelleMake_Up.listaProdXusu.obtenerCompras();
        
        // DEBUG: Ver qué compras se están cargando
        System.out.println("📥 Compras encontradas: " + compras.size());
        for (prodXusu pxu : compras) {
            System.out.println("📦 Compra: " + pxu.getIdprod() + 
                             " - Cantidad: " + pxu.getCantiComprado() +
                             " - Fecha: " + (pxu.getComprado() != null ? pxu.getComprado().format(dateFormatter) : "Sin fecha"));
        }
        
        if (compras.isEmpty()) {
            lblSinCompras.setVisible(true);
            return;
        }
        
        lblSinCompras.setVisible(false);
        
        // Ordenar por fecha de compra (más reciente primero)
        Collections.sort(compras, new Comparator<prodXusu>() {
            @Override
            public int compare(prodXusu c1, prodXusu c2) {
                if (c1.getComprado() == null && c2.getComprado() == null) return 0;
                if (c1.getComprado() == null) return 1;
                if (c2.getComprado() == null) return -1;
                return c2.getComprado().compareTo(c1.getComprado()); // Orden descendente
            }
        });
        
        double totalGastado = 0;
        
        for (prodXusu pxu : compras) {
            producto prod = buscarProductoPorId(pxu.getIdprod());
            if (prod != null && pxu.getComprado() != null && pxu.getCantiComprado() > 0) {
                VBox itemCompra = crearItemCompra(prod, pxu);
                contenedorCompras.getChildren().add(itemCompra);
                
                // Calcular total gastado
                totalGastado += prod.getPrecio() * pxu.getCantiComprado();
                
                // Agregar separador si no es el último
                if (compras.indexOf(pxu) < compras.size() - 1) {
                    Separator separator = new Separator();
                    separator.setPadding(new Insets(5, 0, 5, 0));
                    contenedorCompras.getChildren().add(separator);
                }
            }
        }
        
        // Agregar total gastado
        agregarTotalGastado(totalGastado);
        
        System.out.println("✅ Historial cargado: " + compras.size() + " compras - Total gastado: $" + totalGastado);
    }
    
    private producto buscarProductoPorId(String idprod) {
        if (BelleMake_Up.listaInventario == null || BelleMake_Up.listaInventario.cab == null) {
            System.err.println("❌ Inventario no disponible");
            return null;
        }
        
        com.mycompany.bellemake_up.modelo.nodo<producto> actual = BelleMake_Up.listaInventario.cab;
        while (actual != null) {
            if (actual.info.getIdprod().equals(idprod)) {
                return actual.info;
            }
            actual = actual.sig;
        }
        
        System.err.println("❌ Producto no encontrado en inventario: " + idprod);
        return null;
    }
    
    private VBox crearItemCompra(producto prod, prodXusu pxu) {
        VBox productBox = new VBox(8);
        productBox.setPadding(new Insets(15, 10, 15, 10));
        productBox.setStyle("-fx-background-color: #f8f9fa; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Imagen del producto (si existe)
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        ImageView img = new ImageView();
        img.setFitWidth(60);
        img.setFitHeight(60);
        img.setPreserveRatio(true);
        
        try {
            String rutaImagenRaw = prod.getRutaImagen();
            String nombreArchivo = rutaImagenRaw.replace("@../image/", "");
            String rutaImagen = "/image/" + nombreArchivo;
            URL imagenUrl = getClass().getResource(rutaImagen);
            
            if (imagenUrl != null) {
                Image image = new Image(imagenUrl.toExternalForm());
                img.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("❌ Error cargando imagen: " + e.getMessage());
        }
        
        // Información del producto
        VBox infoBox = new VBox(5);
        
        // Nombre del producto
        Label nombre = new Label(prod.getNomprod());
        nombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-wrap-text: true;");
        
        // Descripción
        Label descripcion = new Label(prod.getDesc());
        descripcion.setStyle("-fx-font-size: 11px; -fx-text-fill: gray; -fx-wrap-text: true;");
        
        infoBox.getChildren().addAll(nombre, descripcion);
        headerBox.getChildren().addAll(img, infoBox);
        
        // Detalles de la compra
        VBox detallesBox = new VBox(5);
        detallesBox.setPadding(new Insets(10, 0, 0, 0));
        
        // Cantidad y precio unitario
        Label cantidad = new Label("Cantidad: " + pxu.getCantiComprado() + 
                                  " • Precio unitario: $" + prod.getPrecio());
        cantidad.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        
        // Subtotal del producto
        double subtotalProducto = prod.getPrecio() * pxu.getCantiComprado();
        Label subtotal = new Label("Subtotal: $" + subtotalProducto);
        subtotal.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #e91e63;");
        
        // Fecha de compra
        Label fecha = new Label("Comprado el: " + pxu.getComprado().format(dateFormatter));
        fecha.setStyle("-fx-font-size: 11px; -fx-text-fill: #2ecc71; -fx-font-weight: bold;");
        
        // Botón para volver a comprar
        HBox botonesBox = new HBox(10);
        botonesBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button btnRecomprar = new Button("Comprar de nuevo");
        btnRecomprar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                             "-fx-font-weight: bold; -fx-pref-width: 120; -fx-font-size: 12px;");
        btnRecomprar.setOnAction(e -> recomprarProducto(prod));
        
        botonesBox.getChildren().add(btnRecomprar);
        
        detallesBox.getChildren().addAll(cantidad, subtotal, fecha, botonesBox);
        
        // Agregar todo al productBox
        productBox.getChildren().addAll(headerBox, detallesBox);
        
        return productBox;
    }
    
    private void agregarTotalGastado(double total) {
        HBox totalBox = new HBox();
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setPadding(new Insets(20, 0, 0, 0));
        
        Label totalLabel = new Label("Total gastado: $" + total);
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: darkblue;");
        
        totalBox.getChildren().add(totalLabel);
        contenedorCompras.getChildren().add(totalBox);
    }
    
    private void recomprarProducto(producto prod) {
        if (prod != null) {
            // Agregar 1 unidad al carrito
            CarritovistaController.agregarAlCarrito(prod, 1);
            
            // Guardar en prodXusu si hay usuario logeado
            if (BelleMake_Up.usuarioActual != null && BelleMake_Up.listaProdXusu != null) {
                try {
                    BelleMake_Up.listaProdXusu.agregarOActualizarCarritoConCantidad(
                        BelleMake_Up.usuarioActual, 
                        prod.getIdprod(), 
                        1
                    );
                    JsonManager.guardarProdXUsu(BelleMake_Up.listaProdXusu);
                    
                    System.out.println("💾 Producto guardado en carrito: " + prod.getNomprod());
                    
                } catch (Exception e) {
                    System.err.println("❌ Error al guardar en carrito: " + e.getMessage());
                }
            }
            
            String mensaje = "🔄 " + prod.getNomprod() + " agregado al carrito";
            System.out.println(mensaje);
            mostrarAlerta("Recompra", mensaje);
        }
    }
    
    @FXML
    private void volverAlCatalogo() {
        try {
            Stage stage = (Stage) contenedorCompras.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/catalogovista.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("🔙 Volviendo al catálogo desde historial...");
        } catch (IOException e) {
            System.err.println("❌ Error al volver al catálogo: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo volver al catálogo: " + e.getMessage());
        }
    }
    
    // ✅ MÉTODO PARA ABRIR HISTORIAL DESDE OTROS CONTROLADORES
    public static void abrirHistorial() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(HistorialComprasvistaController.class.getResource("/fxml/historialComprasvista.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Historial de Compras - Belle Makeup");
            stage.show();
            
            System.out.println("📦 Abriendo historial de compras...");
            
        } catch (IOException e) {
            System.err.println("❌ Error al abrir historial: " + e.getMessage());
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