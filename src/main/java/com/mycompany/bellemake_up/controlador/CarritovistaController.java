package com.mycompany.bellemake_up.controlador;

import com.mycompany.bellemake_up.modelo.producto;
import com.mycompany.bellemake_up.modelo.prodXusu;
import com.mycompany.bellemake_up.modelo.listaProdXusu;
import com.mycompany.bellemake_up.modelo.listaInventario;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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

public class CarritovistaController implements Initializable {

    // Map para almacenar producto -> cantidad
    private static Map<producto, Integer> carrito = new HashMap<>();

    // Variables para integración con prodXusu
    private static listaProdXusu listaProdXUsu;
    private static String usuarioActual;
    private static listaInventario inventario;

    @FXML
    private VBox contenedorItemsCarrito;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblEnvio;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblCarritoVacio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(" Inicializando CarritovistaController...");
        cargarCarritoDesdeUsuario();
        actualizarVistaCarrito();
    }

    // ✅ MÉTODO ESTÁTICO PARA INICIALIZAR DESDE LOGIN/REGISTRO
    public static void inicializarSistema(listaProdXusu prodXUsu, String usuario, listaInventario inv) {
        listaProdXUsu = prodXUsu;
        usuarioActual = usuario;
        inventario = inv;
        System.out.println(" Carrito inicializado para usuario: " + usuario);

        // Cargar carrito del usuario
        cargarCarritoDesdeUsuario();
    }

    // ✅ MÉTODO PARA OBTENER EL CARRITO COMPLETO (PARA CHECKOUT)
    public static Map<producto, Integer> getCarrito() {
        return carrito;
    }

    // ✅ MÉTODO PARA LIMPIAR CARRITO DESPUÉS DE COMPRA
    public static void limpiarCarrito() {
        carrito.clear();
        System.out.println(" Carrito limpiado después de compra");
        
        // También limpiar en prodXusu si hay usuario logeado
        if (listaProdXUsu != null && usuarioActual != null) {
            for (prodXusu pxu : listaProdXUsu.obtenerCarritoConCantidades()) {
                listaProdXUsu.eliminarDelCarrito(usuarioActual, pxu.getIdprod());
            }
            JsonManager.guardarProdXUsu(listaProdXUsu);
        }
    }

    // ✅ MÉTODO PARA ABRIR CHECKOUT
    @FXML
    private void procederPago() {
        if (carrito.isEmpty()) {
            mostrarAlerta("Carrito vacío", "Agrega productos al carrito antes de proceder al pago");
            return;
        }
        
        abrirCheckout();
    }

    private void abrirCheckout() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/checkoutvista.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Checkout - Belle Makeup");
            stage.show();
            
            System.out.println("Abriendo checkout...");
            
        } catch (IOException e) {
            System.err.println(" Error al abrir checkout: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo abrir checkout: " + e.getMessage());
        }
    }

    // ✅ MÉTODO PARA CARGAR CARRITO DESDE USUARIO (CORREGIDO)
    private static void cargarCarritoDesdeUsuario() {
        if (listaProdXUsu == null || usuarioActual == null || inventario == null) {
            System.out.println("  Aún no hay usuario logeado");
            carrito.clear();
            return;
        }

        carrito.clear();

        // Obtener productos en carrito del usuario
        for (prodXusu pxu : listaProdXUsu.obtenerCarritoConCantidades()) {
            // Buscar el producto completo por ID
            producto prod = buscarProductoPorId(pxu.getIdprod());
            if (prod != null && pxu.getCantiComprado() > 0) {
                carrito.put(prod, pxu.getCantiComprado());
                System.out.println(" Cargado desde usuario: " + prod.getNomprod() + " x" + pxu.getCantiComprado());
            }
        }
    }

    // ✅ BUSCAR PRODUCTO POR ID (CORREGIDO)
    private static producto buscarProductoPorId(String idprod) {
        if (inventario == null || inventario.cab == null) {
            System.err.println("Inventario no disponible");
            return null;
        }

        com.mycompany.bellemake_up.modelo.nodo<producto> actual = inventario.cab;
        while (actual != null) {
            if (actual.info.getIdprod().equals(idprod)) {
                return actual.info;
            }
            actual = actual.sig;
        }
        
        System.err.println(" Producto no encontrado en inventario: " + idprod);
        return null;
    }

    //  GUARDAR CARRITO EN USUARIO
    private void guardarCarritoEnUsuario() {
        if (listaProdXUsu == null || usuarioActual == null) {
            System.err.println(" No se puede guardar - usuario no logeado");
            return;
        }

        // Limpiar carrito anterior del usuario
        for (prodXusu pxu : listaProdXUsu.obtenerCarritoConCantidades()) {
            listaProdXUsu.eliminarDelCarrito(usuarioActual, pxu.getIdprod());
        }

        // Guardar productos actuales del carrito
        for (Map.Entry<producto, Integer> entry : carrito.entrySet()) {
            producto p = entry.getKey();
            int cantidad = entry.getValue();

            if (cantidad > 0) {
                listaProdXUsu.agregarOActualizarCarritoConCantidad(usuarioActual, p.getIdprod(), cantidad);
                System.out.println(" Guardado para usuario: " + p.getNomprod() + " x" + cantidad);
            }
        }

        // Guardar en JSON
        JsonManager.guardarProdXUsu(listaProdXUsu);
        System.out.println(" Carrito guardado para usuario: " + usuarioActual);
    }

    // ✅ MÉTODO ESTÁTICO PARA AGREGAR PRODUCTOS
    public static void agregarAlCarrito(producto producto, int cantidad) {
        // Si el producto ya está en el carrito, sumar la cantidad
        if (carrito.containsKey(producto)) {
            int cantidadActual = carrito.get(producto);
            carrito.put(producto, cantidadActual + cantidad);
            System.out.println(" Producto existente, cantidad actualizada: " + (cantidadActual + cantidad));
        } else {
            // Si no existe, agregar nuevo producto
            carrito.put(producto, cantidad);
            System.out.println(" Nuevo producto agregado al carrito: " + producto.getNomprod());
        }

        //  GUARDAR AUTOMÁTICAMENTE SI HAY USUARIO LOGEADO
        if (listaProdXUsu != null && usuarioActual != null) {
            try {
                listaProdXUsu.agregarOActualizarCarritoConCantidad(usuarioActual, producto.getIdprod(), 
                    carrito.get(producto)); // Usar la cantidad actualizada
                
                JsonManager.guardarProdXUsu(listaProdXUsu);
                System.out.println(" Carrito guardado automáticamente");
                
            } catch (Exception e) {
                System.err.println(" Error al guardar carrito automáticamente: " + e.getMessage());
            }
        }
    }

    //  ACTUALIZAR VISTA DEL CARRITO
    private void actualizarVistaCarrito() {
        contenedorItemsCarrito.getChildren().clear();

        if (carrito.isEmpty()) {
            lblCarritoVacio.setVisible(true);
            actualizarTotales();
            return;
        }

        lblCarritoVacio.setVisible(false);

        for (Map.Entry<producto, Integer> entry : carrito.entrySet()) {
            producto p = entry.getKey();
            int cantidad = entry.getValue();
            HBox itemCard = crearItemCarrito(p, cantidad);
            contenedorItemsCarrito.getChildren().add(itemCard);
        }

        actualizarTotales();
    }

    //  CREAR ITEM DEL CARRITO
    private HBox crearItemCarrito(producto p, int cantidad) {
        HBox card = new HBox(15);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: #e0e0e0;");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefHeight(120);

        // Imagen del producto
        ImageView img = new ImageView();
        img.setFitWidth(80);
        img.setFitHeight(80);
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
            System.err.println(" Error cargando imagen del carrito: " + e.getMessage());
        }

        // Información del producto
        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(300);

        Label nombre = new Label(p.getNomprod());
        nombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-wrap-text: true;");

        Label precioUnitario = new Label("$" + p.getPrecio() + " c/u");
        precioUnitario.setStyle("-fx-font-size: 14px; -fx-text-fill: #E91E63;");

        Label categoria = new Label(p.getCategoria());
        categoria.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        infoBox.getChildren().addAll(nombre, precioUnitario, categoria);

        // Controles de cantidad
        VBox controlesBox = new VBox(5);
        controlesBox.setAlignment(Pos.CENTER);

        HBox controlesCantidad = new HBox(10);
        controlesCantidad.setAlignment(Pos.CENTER);

        Button btnMenos = new Button("-");
        btnMenos.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 30; -fx-min-height: 30;");
        btnMenos.setOnAction(e -> {
            if (cantidad > 1) {
                carrito.put(p, cantidad - 1);
                actualizarVistaCarrito();
                guardarCarritoEnUsuario(); // Guardar cambios
            }
        });

        Label lblCantidad = new Label(String.valueOf(cantidad));
        lblCantidad.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 30; -fx-alignment: center;");

        Button btnMas = new Button("+");
        btnMas.setStyle("-fx-background-color: #FFB6C1; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 30; -fx-min-height: 30;");
        btnMas.setOnAction(e -> {
            if (cantidad < p.getStock()) {
                carrito.put(p, cantidad + 1);
                actualizarVistaCarrito();
                guardarCarritoEnUsuario(); // Guardar cambios
            } else {
                mostrarAlerta("Stock máximo", "No hay más stock disponible de este producto");
            }
        });

        controlesCantidad.getChildren().addAll(btnMenos, lblCantidad, btnMas);
        controlesBox.getChildren().add(controlesCantidad);

        // Subtotal
        double subtotalItem = p.getPrecio() * cantidad;
        Label lblSubtotalItem = new Label("$" + subtotalItem);
        lblSubtotalItem.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #E91E63;");

        // Botón eliminar
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-width: 80;");
        btnEliminar.setOnAction(e -> {
            carrito.remove(p);
            actualizarVistaCarrito();
            guardarCarritoEnUsuario(); // Guardar cambios
            mostrarAlerta("Producto eliminado", p.getNomprod() + " ha sido removido del carrito");
        });

        // Organizar en el card
        card.getChildren().addAll(img, infoBox, controlesBox, lblSubtotalItem, btnEliminar);

        return card;
    }

    //  ACTUALIZAR TOTALES
    private void actualizarTotales() {
        double subtotal = 0;
        for (Map.Entry<producto, Integer> entry : carrito.entrySet()) {
            producto p = entry.getKey();
            int cantidad = entry.getValue();
            subtotal += p.getPrecio() * cantidad;
        }

        double envio = 10000; // Costo fijo de envío
        double total = subtotal + envio;

        lblSubtotal.setText("$" + subtotal);
        lblTotal.setText("$" + total);

        System.out.println(" Carrito actualizado - Subtotal: $" + subtotal + ", Total: $" + total);
    }

    //  VACIAR CARRITO
    @FXML
    private void vaciarCarrito() {
        if (!carrito.isEmpty()) {
            carrito.clear();
            guardarCarritoEnUsuario(); // Guardar cambios
            actualizarVistaCarrito();
            mostrarAlerta("Carrito vaciado", "Todos los productos han sido removidos del carrito");
            System.out.println("️ Carrito vaciado");
        }
    }

    // VOLVER AL CATÁLOGO
    @FXML
    private void volverAlCatalogo() {
        try {
            // Guardar carrito antes de salir
            guardarCarritoEnUsuario();

            Stage stage = (Stage) contenedorItemsCarrito.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/catalogovista.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println(" Volviendo al catálogo...");
        } catch (IOException e) {
            System.err.println(" Error al volver al catálogo: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo volver al catálogo: " + e.getMessage());
        }
    }

    //  MÉTODOS GETTER ESTÁTICOS PARA CHECKOUT
    public static int getCantidadTotalItems() {
        int total = 0;
        for (int cantidad : carrito.values()) {
            total += cantidad;
        }
        return total;
    }

    public static double getTotalCarrito() {
        double total = 0;
        for (Map.Entry<producto, Integer> entry : carrito.entrySet()) {
            producto p = entry.getKey();
            int cantidad = entry.getValue();
            total += p.getPrecio() * cantidad;
        }
        return total;
    }

    //  ALERTA
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}