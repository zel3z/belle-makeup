package com.mycompany.bellemake_up.controlador;

import com.mycompany.bellemake_up.BelleMake_Up;
import com.mycompany.bellemake_up.modelo.listaInventario;
import com.mycompany.bellemake_up.modelo.nodo;
import com.mycompany.bellemake_up.modelo.producto;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CatalogovistaController implements Initializable {

    private listaInventario inventario = new listaInventario();
    private List<producto> productosOriginales = new ArrayList<>();

    @FXML
    private VBox contenedorProductos;
    @FXML
    private ComboBox<String> comboFiltros;
    @FXML
    private ComboBox<String> comboCategorias;
    @FXML
    private TextField txtBuscador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inicializando CatalogovistaController...");

        if (contenedorProductos == null) {
            System.err.println("ERROR: contenedorProductos no fue inyectado desde FXML");
            return;
        }

        // Usar el inventario global en lugar de cargarlo de nuevo
        inventario = BelleMake_Up.listaInventario;

        cargarProductosOriginales();
        inicializarFiltros();
        inicializarCategorias();
        mostrarProductos();
        configurarEventos();
    }

    private void cargarProductosOriginales() {
        productosOriginales.clear();
        nodo<producto> actual = inventario.cab;
        while (actual != null) {
            productosOriginales.add(actual.info);
            actual = actual.sig;
        }
    }

    private void inicializarFiltros() {
        ObservableList<String> opcionesFiltro = FXCollections.observableArrayList(
                "Todos los productos",
                "Menor precio",
                "Mayor precio"
        );
        comboFiltros.setItems(opcionesFiltro);
        comboFiltros.setValue("Todos los productos");
    }

    private void inicializarCategorias() {
        Set<String> categoriasUnicas = new HashSet<>();
        nodo<producto> actual = inventario.cab;

        while (actual != null) {
            categoriasUnicas.add(actual.info.getCategoria());
            actual = actual.sig;
        }

        List<String> listaCategorias = new ArrayList<>(categoriasUnicas);
        Collections.sort(listaCategorias);

        ObservableList<String> opcionesCategoria = FXCollections.observableArrayList(
                "Todas las categorías"
        );
        opcionesCategoria.addAll(listaCategorias);

        comboCategorias.setItems(opcionesCategoria);
        comboCategorias.setValue("Todas las categorías");
    }

    private void configurarEventos() {
        // Evento para el combo de filtros de precio
        comboFiltros.setOnAction(e -> aplicarFiltros());

        // Evento para el combo de categorías
        comboCategorias.setOnAction(e -> aplicarFiltros());

        // Evento para el buscador (buscar al presionar Enter o cambiar texto)
        txtBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
            aplicarFiltros();
        });
    }

    private void aplicarFiltros() {
        List<producto> productosFiltrados = new ArrayList<>(productosOriginales);

        // Aplicar filtro de búsqueda por texto
        String textoBusqueda = txtBuscador.getText().toLowerCase().trim();
        if (!textoBusqueda.isEmpty()) {
            productosFiltrados.removeIf(producto
                    -> !producto.getNomprod().toLowerCase().contains(textoBusqueda)
                    && !producto.getCategoria().toLowerCase().contains(textoBusqueda)
                    && !producto.getDesc().toLowerCase().contains(textoBusqueda)
            );
        }

        // Aplicar filtro de categoría
        String categoriaSeleccionada = comboCategorias.getValue();
        if (categoriaSeleccionada != null && !categoriaSeleccionada.equals("Todas las categorías")) {
            productosFiltrados.removeIf(producto
                    -> !producto.getCategoria().equals(categoriaSeleccionada)
            );
        }

        // Aplicar filtro de ordenamiento por precio
        String filtroPrecio = comboFiltros.getValue();
        if (filtroPrecio != null) {
            switch (filtroPrecio) {
                case "Menor precio":
                    Collections.sort(productosFiltrados, Comparator.comparingDouble(producto::getPrecio));
                    break;
                case "Mayor precio":
                    Collections.sort(productosFiltrados, Comparator.comparingDouble(producto::getPrecio).reversed());
                    break;
                // "Todos los productos" no requiere ordenamiento específico
            }
        }

        mostrarProductosFiltrados(productosFiltrados);
    }

    private void mostrarProductosFiltrados(List<producto> productos) {
        // Limpiar productos actuales
        if (contenedorProductos.getChildren().size() > 2) {
            contenedorProductos.getChildren().remove(2, contenedorProductos.getChildren().size());
        }

        // Crear GridPane para organizar los productos en filas de 4
        GridPane gridProductos = new GridPane();
        gridProductos.setPadding(new Insets(20));
        gridProductos.setHgap(20);
        gridProductos.setVgap(20);
        gridProductos.setAlignment(Pos.TOP_CENTER);

        // Configurar para que las columnas se expandan proporcionalmente
        for (int i = 0; i < 4; i++) {
            gridProductos.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints());
            gridProductos.getColumnConstraints().get(i).setHgrow(Priority.ALWAYS);
            gridProductos.getColumnConstraints().get(i).setFillWidth(true);
        }

        int contador = 0;
        int columna = 0;
        int fila = 0;

        for (producto p : productos) {
            VBox tarjeta = crearTarjetaProducto(p);
            gridProductos.add(tarjeta, columna, fila);

            contador++;
            columna++;

            // Cuando llegamos a 4 productos, pasamos a la siguiente fila
            if (columna == 4) {
                columna = 0;
                fila++;
            }
        }

        // Agregar el grid al contenedor principal
        contenedorProductos.getChildren().add(gridProductos);
        VBox.setVgrow(gridProductos, Priority.ALWAYS);

        System.out.println("Productos mostrados después de filtros: " + contador);

        // Mostrar mensaje si no hay resultados
        if (contador == 0) {
            mostrarMensajeSinResultados();
        }
    }

    private void mostrarMensajeSinResultados() {
        Label mensaje = new Label("No se encontraron productos que coincidan con tu búsqueda.");
        mensaje.setStyle("-fx-font-size: 16px; -fx-text-fill: #666; -fx-padding: 40px;");
        mensaje.setAlignment(Pos.CENTER);

        VBox contenedorMensaje = new VBox(mensaje);
        contenedorMensaje.setAlignment(Pos.CENTER);
        contenedorMensaje.setPrefHeight(200);

        contenedorProductos.getChildren().add(contenedorMensaje);
    }

    private void mostrarProductos() {
        mostrarProductosFiltrados(productosOriginales);
    }

    private VBox crearTarjetaProducto(producto p) {
        VBox tarjeta = new VBox();
        tarjeta.setSpacing(10);
        tarjeta.setPadding(new Insets(15));
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #FFFFF;");
        tarjeta.setMaxWidth(250);
        tarjeta.setPrefWidth(250);

        // Hacer que la tarjeta sea responsive
        tarjeta.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(tarjeta, Priority.ALWAYS);

        // Imagen - centrada y con tamaño proporcional
        ImageView img = new ImageView();
        img.setFitWidth(150);
        img.setFitHeight(150);
        img.setPreserveRatio(true);
        img.setSmooth(true);
        img.setCache(true);

        try {
            String rutaImagenRaw = p.getRutaImagen();
            String nombreArchivo = rutaImagenRaw.replace("@../image/", "");
            String rutaImagen = "/image/" + nombreArchivo;

            URL imagenUrl = getClass().getResource(rutaImagen);

            if (imagenUrl != null) {
                Image image = new Image(imagenUrl.toExternalForm());
                img.setImage(image);
            } else {
                System.err.println(" No se encontró la imagen: " + rutaImagen);
                URL defaultImage = getClass().getResource("/image/default.png");
                if (defaultImage != null) {
                    img.setImage(new Image(defaultImage.toExternalForm()));
                }
            }
        } catch (Exception e) {
            System.err.println(" Error cargando imagen de: " + p.getNomprod());
            e.printStackTrace();
        }

        // Información del producto - debajo de la imagen
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPrefWidth(200);
        infoBox.setMaxWidth(200);

        Label nombre = new Label(p.getNomprod());
        nombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-wrap-text: true;");
        nombre.setMaxWidth(200);
        nombre.setWrapText(true);
        nombre.setAlignment(Pos.CENTER);

        Label precio = new Label("$" + p.getPrecio());
        precio.setStyle("-fx-font-size: 16px; -fx-text-fill: #e91e63; -fx-font-weight: bold;");
        precio.setAlignment(Pos.CENTER);

        Label categoria = new Label(p.getCategoria());
        categoria.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        categoria.setAlignment(Pos.CENTER);

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
            String ruta = "/fxml/vistaDetallesProducto.fxml";
            URL fxmlUrl = getClass().getResource(ruta);

            if (fxmlUrl == null) {
                System.err.println(" ERROR: El FXML no se encuentra en: " + ruta);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            VistaDetallesProductoController controller = loader.getController();
            controller.setProducto(p);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Detalles - " + p.getNomprod());
            stage.show();

        } catch (Exception e) {
            System.err.println(" ERROR CRÍTICO al abrir detalles:");
            e.printStackTrace();

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo abrir los detalles del producto");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }
    // ==================== MÉTODOS DEL CARRITO ====================

    /**
     * Método para agregar producto al carrito directamente desde el catálogo
     */
    private void agregarAlCarritoDesdeCatalogo(producto p) {
        // Agregar 1 unidad por defecto
        CarritovistaController.agregarAlCarrito(p, 1);
        String mensaje = "" + p.getNomprod() + " agregado al carrito";
        System.out.println(mensaje);
        mostrarAlerta("Carrito", mensaje);
    }

    /**
     * Método para abrir la vista del carrito
     */
    @FXML
    private void abrirCarrito() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/carritovista.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Mi Carrito - Belle Makeup");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al abrir el carrito: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo abrir el carrito: " + e.getMessage());
        }
    }

    /**
     * Método para mostrar el número de items en el carrito (útil para mostrar
     * en la interfaz)
     */
    public static int getCantidadItemsCarrito() {
        return CarritovistaController.getCantidadTotalItems();
    }

    /**
     * Método para obtener el total del carrito
     */
    public static double getTotalCarrito() {
        return CarritovistaController.getTotalCarrito();
    }

    /**
     * Método para mostrar alertas
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
 * Método para abrir la vista de favoritos
 */
    @FXML
    private void abrirFavoritos() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/favoritosvista.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Mis Favoritos - Belle Makeup");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al abrir favoritos: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo abrir favoritos: " + e.getMessage());
        }
    }
    /**
 * Método para abrir el historial de compras
 */
    @FXML
    private void abrirHistorial() {
        try {
            // Verificar si hay usuario logeado
            if (BelleMake_Up.usuarioActual == null) {
                mostrarAlerta("Inicia sesión", "Debes iniciar sesión para ver tu historial de compras");
                return;
            }

            HistorialComprasvistaController.abrirHistorial();

        } catch (Exception e) {
            System.err.println("Error al abrir historial: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo abrir historial: " + e.getMessage());
        }
    }
    // ==================== MÉTODOS EXISTENTES ====================
    @FXML
    private void volverPreview(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/previewvista.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        stage.setMaximized(true);
    }

    // Métodos para limpiar filtros
    @FXML
    private void limpiarFiltros() {
        comboFiltros.setValue("Todos los productos");
        comboCategorias.setValue("Todas las categorías");
        txtBuscador.clear();
        mostrarProductos();
    }
}
