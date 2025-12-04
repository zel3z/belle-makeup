package com.mycompany.bellemake_up.controlador;

import com.mycompany.bellemake_up.modelo.producto;
import com.mycompany.bellemake_up.BelleMake_Up;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class VistaDetallesProductoController {

    @FXML
    private ImageView imgProducto;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblPrecio;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private Button btnMenos;
    @FXML
    private Button btnMas;
    @FXML
    private Label lblCantidad;
    @FXML
    private Button btnFavoritos;
    @FXML
    private Button btnComprar;
    @FXML
    private Button btnCarrito;

    private int cantidad = 1;
    private producto prod;

    @FXML
    public void initialize() {
        System.out.println("Inicializando VistaDetallesProductoController...");
        actualizarCantidad();

        // Configurar eventos
        configurarEventos();
    }

    private void configurarEventos() {
        // Aumentar cantidad
        btnMas.setOnAction(e -> {
            if (prod != null && cantidad < prod.getStock()) {
                cantidad++;
                actualizarCantidad();
                System.out.println("Cantidad aumentada a: " + cantidad);
            } else {
                mostrarAlerta("Stock máximo", "No hay más stock disponible");
            }
        });

        // Disminuir cantidad
        btnMenos.setOnAction(e -> {
            if (cantidad > 1) {
                cantidad--;
                actualizarCantidad();
                System.out.println("Cantidad disminuida a: " + cantidad);
            }
        });

        // Acciones principales
        btnCarrito.setOnAction(e -> agregarAlCarrito());
        btnComprar.setOnAction(e -> comprarAhora());
        btnFavoritos.setOnAction(e -> agregarAFavoritos());
    }

    private void actualizarCantidad() {
        lblCantidad.setText(String.valueOf(cantidad));
    }

    public void setProducto(producto p) {
        this.prod = p;
        this.cantidad = 1; // Resetear cantidad
        actualizarCantidad();

        if (p != null) {
            cargarDatosProducto();
        }
    }

    private void cargarDatosProducto() {
        // Datos básicos
        lblNombre.setText(prod.getNomprod());
        lblPrecio.setText("$" + prod.getPrecio());
        txtDescripcion.setText(prod.getDesc());

        // Cargar imagen
        cargarImagenProducto();

        System.out.println(" Producto cargado en detalles: " + prod.getNomprod());
    }

    private void cargarImagenProducto() {
        try {
            // Obtener la ruta del JSON y limpiarla
            String rutaImagenRaw = prod.getRutaImagen();
            System.out.println(" Ruta original del JSON: " + rutaImagenRaw);

            // Limpiar la ruta - eliminar "@../image/" 
            String nombreArchivo = rutaImagenRaw.replace("@../image/", "");

            // Ruta CORRECTA - las imágenes están en /image/
            String rutaImagen = "/image/" + nombreArchivo;

            System.out.println(" Ruta final de imagen: " + rutaImagen);

            URL imagenUrl = getClass().getResource(rutaImagen);

            if (imagenUrl != null) {
                Image image = new Image(imagenUrl.toExternalForm());
                imgProducto.setImage(image);
                System.out.println("Imagen cargada exitosamente: " + nombreArchivo);
            } else {
                System.err.println(" No se encontró la imagen: " + rutaImagen);

                // Intentar cargar imagen por defecto
                cargarImagenPorDefecto();
            }
        } catch (Exception e) {
            System.err.println(" Error cargando imagen: " + e.getMessage());
            e.printStackTrace();
            cargarImagenPorDefecto();
        }
    }

    private void cargarImagenPorDefecto() {
        try {
            // Probar diferentes rutas para la imagen por defecto
            String[] posiblesRutas = {
                "/image/default.png",
                "/com/mycompany/bellemake_up/imagenes/default.png",
                "/font/default.png"
            };

            for (String ruta : posiblesRutas) {
                URL defaultImageUrl = getClass().getResource(ruta);
                if (defaultImageUrl != null) {
                    Image defaultImage = new Image(defaultImageUrl.toExternalForm());
                    imgProducto.setImage(defaultImage);
                    System.out.println(" Imagen por defecto cargada desde: " + ruta);
                    return;
                }
            }

            System.err.println("️ No se pudo encontrar ninguna imagen por defecto");

        } catch (Exception e) {
            System.err.println(" Error cargando imagen por defecto: " + e.getMessage());
        }
    }

    private void agregarAlCarrito() {
        if (prod != null) {
            // AGREGAR AL CARRITO Y GUARDAR EN PRODXUSU
            CarritovistaController.agregarAlCarrito(prod, cantidad);

            // GUARDAR DIRECTAMENTE EN PRODXUSU SI HAY USUARIO LOGEADO
            if (BelleMake_Up.usuarioActual != null && BelleMake_Up.listaProdXusu != null) {
                try {
                    // Agregar/actualizar en prodXusu
                    BelleMake_Up.listaProdXusu.agregarOActualizarCarritoConCantidad(
                            BelleMake_Up.usuarioActual,
                            prod.getIdprod(),
                            cantidad
                    );

                    // Guardar en JSON
                    JsonManager.guardarProdXUsu(BelleMake_Up.listaProdXusu);

                    System.out.println(" Producto guardado en prodXusu: " + prod.getNomprod() + " x" + cantidad);

                } catch (Exception e) {
                    System.err.println(" Error al guardar en prodXusu: " + e.getMessage());
                }
            }

            String mensaje = "" + prod.getNomprod() + " x" + cantidad + " agregado al carrito";
            System.out.println(mensaje);
            mostrarAlerta("Carrito", mensaje);
        }
    }

private void comprarAhora() {
    if (prod != null) {
        try {
            // GUARDAR LA COMPRA DIRECTA EN VARIABLES GLOBALES
            BelleMake_Up.compraDirectaProducto = prod;
            BelleMake_Up.compraDirectaCantidad = cantidad;
            
            System.out.println("Compra directa guardada: " + prod.getNomprod() + " x" + cantidad);
            
            // ✅ Abrir checkout directamente
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/checkoutvista.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Checkout - Belle Makeup");
            stage.show();

            System.out.println(" Abriendo checkout desde detalles...");

            // ✅ Cerrar ventana actual de detalles
            cerrarVentana();

        } catch (IOException e) {
            System.err.println(" Error al abrir checkout: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo abrir checkout: " + e.getMessage());
        }
    }
}

    private void agregarAFavoritos() {
        if (prod != null) {
            //  AGREGAR A FAVORITOS EN PRODXUSU
            if (BelleMake_Up.usuarioActual != null && BelleMake_Up.listaProdXusu != null) {
                try {
                    boolean agregado = BelleMake_Up.listaProdXusu.agregarOActualizarFavorito(
                            BelleMake_Up.usuarioActual,
                            prod.getIdprod()
                    );

                    // Guardar en JSON
                    JsonManager.guardarProdXUsu(BelleMake_Up.listaProdXusu);

                    String mensaje = agregado
                            ? "️ " + prod.getNomprod() + " agregado a favoritos"
                            : " " + prod.getNomprod() + " eliminado de favoritos";

                    System.out.println(mensaje);
                    mostrarAlerta("Favoritos", mensaje);

                } catch (Exception e) {
                    System.err.println(" Error al agregar a favoritos: " + e.getMessage());
                    mostrarAlerta("Error", "No se pudo agregar a favoritos");
                }
            } else {
                mostrarAlerta("Info", "Inicia sesión para usar favoritos");
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void cerrarVentana() {
        System.out.println("Cerrando ventana de detalles...");
        Stage stage = (Stage) lblNombre.getScene().getWindow();
        stage.close();
    }
}
