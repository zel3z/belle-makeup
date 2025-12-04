package com.mycompany.bellemake_up.controlador;

import com.mycompany.bellemake_up.modelo.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.layout.GridPane;

public class AdminvistaController implements Initializable {

    // Header
    @FXML
    private Button btnRegresarPreview;

    // Estadísticas ROJAS
    @FXML
    private Label lblTotalProductos;
    @FXML
    private Label lblTotalUsuarios;

    // Formulario - Botones ROJOS
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private Button btnCatMaquillaje;
    @FXML
    private Button btnCatCuidadoFacial;
    @FXML
    private Button btnCatAccesorios;
    @FXML
    private Button btnElegirImagen;
    @FXML
    private Label lblArchivo;
    @FXML
    private Button btnAgregarProducto;

    // Tabla - Ancho completo
    @FXML
    private TableView<producto> tablaProductos;
    private ObservableList<producto> productos = FXCollections.observableArrayList();

    // Variables
    private listaInventario inventario;
    private listaUsuario listaUsuarios;
    private String categoriaSeleccionada = "Maquillaje";
    private String rutaImagenSeleccionada = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("=== INICIALIZANDO PANEL ADMIN ===");

        // 1. Cargar datos
        cargarDatos();

        // 2. Configurar botones ROJOS
        configurarBotonesRojos();

        // 3. Configurar tabla con ancho completo
        configurarTablaAnchoCompleto();

        // 4. Configurar eventos
        configurarEventos();

        // 5. Actualizar estadísticas ROJAS
        actualizarEstadisticasRojas();

        System.out.println("Panel admin inicializado correctamente");
    }

    private void cargarDatos() {
        // Cargar inventario
        inventario = new listaInventario();
        JsonManager.cargarInventario(inventario);

        // Cargar usuarios
        listaUsuarios = new listaUsuario();
        JsonManager.cargarUsuarios(listaUsuarios);

        // Cargar productos a la ObservableList
        ArrayList<producto> productosLista = inventario.toArrayList();
        productos.addAll(productosLista);

        System.out.println("Productos cargados: " + productos.size());
        System.out.println("Usuarios cargados: " + (listaUsuarios != null ? listaUsuarios.toArrayList().size() : 0));
    }

    private void configurarBotonesRojos() {
        // Configurar estilo inicial de botones de categoría (Maquillaje seleccionado ROJO)
        btnCatMaquillaje.setStyle("-fx-background-color: #DF0909; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");
        btnCatCuidadoFacial.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 8 16;");
        btnCatAccesorios.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 8 16;");

        // Configurar botón de elegir imagen ROJO
        btnElegirImagen.setStyle("-fx-background-color: #DF0909; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");

        // Configurar botón agregar producto ROJO
        btnAgregarProducto.setStyle("-fx-background-color: #DF0909; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 40; -fx-font-size: 16px;");

        // Eventos para cambiar categoría (ROJO cuando está seleccionado)
        btnCatMaquillaje.setOnAction(e -> {
            categoriaSeleccionada = "Maquillaje";
            btnCatMaquillaje.setStyle("-fx-background-color: #DF0909; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");
            btnCatCuidadoFacial.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 8 16;");
            btnCatAccesorios.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 8 16;");
        });

        btnCatCuidadoFacial.setOnAction(e -> {
            categoriaSeleccionada = "Cuidado Facial";
            btnCatMaquillaje.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 8 16;");
            btnCatCuidadoFacial.setStyle("-fx-background-color: #DF0909; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");
            btnCatAccesorios.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 8 16;");
        });

        btnCatAccesorios.setOnAction(e -> {
            categoriaSeleccionada = "Accesorios";
            btnCatMaquillaje.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 8 16;");
            btnCatCuidadoFacial.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 8 16;");
            btnCatAccesorios.setStyle("-fx-background-color: #DF0909; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");
        });

        // Botón regresar a Preview
        btnRegresarPreview.setOnAction(e -> {
            regresarAPreview();
        });
    }

    private void configurarTablaAnchoCompleto() {
        // Limpiar columnas existentes
        tablaProductos.getColumns().clear();

        // Configurar tabla para usar ancho completo
        tablaProductos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Calcular ancho de columnas proporcional al contenido
        int totalWidth = 1350; // Ancho total disponible
        double[] columnWidths = {0.10, 0.25, 0.15, 0.10, 0.10, 0.10, 0.10}; // Proporciones

        // Columna ID
        TableColumn<producto, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idprod"));
        colId.setPrefWidth(totalWidth * columnWidths[0]);
        colId.setStyle("-fx-alignment: CENTER;");

        // Columna Nombre
        TableColumn<producto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nomprod"));
        colNombre.setPrefWidth(totalWidth * columnWidths[1]);
        colNombre.setStyle("-fx-alignment: CENTER_LEFT;");

        // Columna Categoría
        TableColumn<producto, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCategoria.setPrefWidth(totalWidth * columnWidths[2]);
        colCategoria.setStyle("-fx-alignment: CENTER;");

        // Columna Precio
        TableColumn<producto, Float> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPrecio.setPrefWidth(totalWidth * columnWidths[3]);
        colPrecio.setCellFactory(column -> new TableCell<producto, Float>() {
            @Override
            protected void updateItem(Float precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty || precio == null) {
                    setText(null);
                } else {
                    setText(String.format("$%,.0f", precio));
                    setStyle("-fx-alignment: CENTER_RIGHT; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");
                }
            }
        });

        // Columna Stock
        TableColumn<producto, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStock.setPrefWidth(totalWidth * columnWidths[4]);
        colStock.setCellFactory(column -> new TableCell<producto, Integer>() {
            @Override
            protected void updateItem(Integer stock, boolean empty) {
                super.updateItem(stock, empty);
                if (empty || stock == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(stock));
                    setStyle("-fx-alignment: CENTER;");
                    if (stock < 10) {
                        setTextFill(Color.RED);
                        setStyle("-fx-font-weight: bold; -fx-alignment: CENTER;");
                    } else if (stock < 20) {
                        setTextFill(Color.ORANGE);
                        setStyle("-fx-alignment: CENTER;");
                    } else {
                        setTextFill(Color.GREEN);
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            }
        });

        // Columna Vendidos
        TableColumn<producto, Integer> colVendidos = new TableColumn<>("Vendidos");
        colVendidos.setCellValueFactory(new PropertyValueFactory<>("vendidos"));
        colVendidos.setPrefWidth(totalWidth * columnWidths[5]);
        colVendidos.setCellFactory(column -> new TableCell<producto, Integer>() {
            @Override
            protected void updateItem(Integer vendidos, boolean empty) {
                super.updateItem(vendidos, empty);
                if (empty || vendidos == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(vendidos));
                    setStyle("-fx-alignment: CENTER; -fx-text-fill: #DF0909; -fx-font-weight: bold;");
                }
            }
        });

        // Columna Acciones (con botones ROJOS)
        TableColumn<producto, String> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setPrefWidth(totalWidth * columnWidths[6]);
        colAcciones.setCellFactory(param -> new TableCell<producto, String>() {
            private final Button btnEditar = new Button("✏️ Editar");
            private final Button btnEliminar = new Button("🗑️ Eliminar");

            {
                // Botones ROJOS
                btnEditar.setStyle("-fx-background-color: #DF0909; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 5 10;");
                btnEliminar.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 5 10;");

                btnEditar.setOnAction(e -> {
                    producto prod = getTableView().getItems().get(getIndex());
                    editarProducto(prod);
                });

                btnEliminar.setOnAction(e -> {
                    producto prod = getTableView().getItems().get(getIndex());
                    eliminarProducto(prod);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox botones = new HBox(10, btnEditar, btnEliminar);
                    botones.setAlignment(Pos.CENTER);
                    setGraphic(botones);
                }
            }
        });

        // Agregar todas las columnas
        tablaProductos.getColumns().addAll(colId, colNombre, colCategoria, colPrecio, colStock, colVendidos, colAcciones);

        // Establecer datos
        tablaProductos.setItems(productos);

        // Configurar estilo de tabla
        tablaProductos.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5;");
    }

    private void configurarEventos() {
        // Botón elegir imagen
        btnElegirImagen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
            );
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                rutaImagenSeleccionada = file.getAbsolutePath();
                lblArchivo.setText(file.getName());
            }
        });

        // Botón agregar producto
        btnAgregarProducto.setOnAction(e -> {
            agregarProducto();
        });
    }

    private void actualizarEstadisticasRojas() {
        // Las estadísticas ya tienen estilo rojo desde el FXML
        // Solo actualizamos los valores

        // 1. Total de Productos
        int totalProductos = productos.size();
        lblTotalProductos.setText(String.valueOf(totalProductos));

        // 2. Total de Usuarios
        int totalUsuarios = calcularTotalUsuarios();
        lblTotalUsuarios.setText(String.valueOf(totalUsuarios));

        System.out.println("Estadísticas ROJAS actualizadas:");
        System.out.println("   Productos: " + totalProductos);
        System.out.println("   Usuarios: " + totalUsuarios);
    }

    private int calcularTotalUsuarios() {
        if (listaUsuarios != null) {
            ArrayList<usuario> usuarios = listaUsuarios.toArrayList();
            return usuarios.size();
        }
        return 0;
    }

    private void regresarAPreview() {
        try {
            System.out.println("Regresando a Preview...");

            // Cerrar ventana actual
            Stage stageActual = (Stage) btnRegresarPreview.getScene().getWindow();
            stageActual.close();

            // Abrir Preview
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/previewvista.fxml"));
            Stage previewStage = new Stage();
            previewStage.setScene(new Scene(root));
            previewStage.setTitle("Belle Make Up - Inicio");
            previewStage.setMaximized(true);
            previewStage.show();

        } catch (IOException e) {
            System.err.println("Error al regresar a Preview: " + e.getMessage());
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la vista principal", Alert.AlertType.ERROR);
        }
    }

    // Los métodos agregarProducto(), editarProducto(), eliminarProducto(), 
    // productoExiste(), generarIdProducto(), limpiarFormulario(), mostrarAlerta()
    // se mantienen iguales que en tu versión anterior
    private void agregarProducto() {
        try {
            // Obtener valores
            String nombre = txtNombreProducto.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            String stockStr = txtStock.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            // Validaciones básicas
            if (nombre.isEmpty()) {
                mostrarAlerta("Error", "El nombre del producto es obligatorio", Alert.AlertType.ERROR);
                return;
            }

            if (precioStr.isEmpty()) {
                mostrarAlerta("Error", "El precio es obligatorio", Alert.AlertType.ERROR);
                return;
            }

            if (stockStr.isEmpty()) {
                mostrarAlerta("Error", "El stock es obligatorio", Alert.AlertType.ERROR);
                return;
            }

            float precio = Float.parseFloat(precioStr);
            int stock = Integer.parseInt(stockStr);

            if (precio <= 0) {
                mostrarAlerta("Error", "El precio debe ser mayor a 0", Alert.AlertType.ERROR);
                return;
            }

            if (stock < 0) {
                mostrarAlerta("Error", "El stock no puede ser negativo", Alert.AlertType.ERROR);
                return;
            }

            // Verificar si ya existe
            if (productoExiste(nombre)) {
                mostrarAlerta("Error", "Ya existe un producto con ese nombre", Alert.AlertType.ERROR);
                return;
            }

            // Generar ID
            String idProd = generarIdProducto();

            // Crear producto
            producto nuevoProducto = new producto(
                    idProd,
                    nombre,
                    precio,
                    descripcion,
                    categoriaSeleccionada,
                    stock,
                    0, // vendidos inicialmente 0
                    rutaImagenSeleccionada.isEmpty() ? "@../image/default.png" : rutaImagenSeleccionada
            );

            // Agregar al inventario
            inventario.agregar(nuevoProducto);

            // Agregar a la lista observable
            productos.add(nuevoProducto);

            // Guardar en JSON
            JsonManager.guardarInventario(inventario);

            // Limpiar formulario
            limpiarFormulario();

            // Actualizar interfaz
            tablaProductos.refresh();
            actualizarEstadisticasRojas(); // Actualizar estadísticas

            mostrarAlerta("Éxito",
                    "Producto agregado correctamente\n"
                    + "Nombre: " + nombre + "\n"
                    + "Precio: $" + String.format("%,.0f", precio) + "\n"
                    + "Categoría: " + categoriaSeleccionada + "\n"
                    + "ID: " + idProd,
                    Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Precio y stock deben ser números válidos", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean productoExiste(String nombre) {
        for (producto p : productos) {
            if (p.getNomprod().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    private String generarIdProducto() {
        int maxId = 0;
        for (producto p : productos) {
            try {
                int id = Integer.parseInt(p.getIdprod());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Ignorar si el ID no es numérico
            }
        }
        return String.valueOf(maxId + 1);
    }

    private void limpiarFormulario() {
        txtNombreProducto.clear();
        txtPrecio.clear();
        txtStock.clear();
        txtDescripcion.clear();
        lblArchivo.setText("No se eligió ningún archivo");
        rutaImagenSeleccionada = "";

        // Resetear categoría a Maquillaje (ROJO)
        categoriaSeleccionada = "Maquillaje";
        btnCatMaquillaje.setStyle("-fx-background-color: #DF0909; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16;");
        btnCatCuidadoFacial.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 8 16;");
        btnCatAccesorios.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333; -fx-font-weight: bold; -fx-padding: 8 16;");
    }

    private void editarProducto(producto productoEditado) {
        // Guardar el ID original para búsqueda
        String idOriginal = productoEditado.getIdprod();
        System.out.println("=== INICIANDO EDICIÓN DE PRODUCTO ===");
        System.out.println("ID Producto: " + idOriginal);
        System.out.println("Nombre actual: " + productoEditado.getNomprod());

        // Crear diálogo
        Dialog<producto> dialog = new Dialog<>();
        dialog.setTitle("✏️ Editar Producto");
        dialog.setHeaderText("Editando: " + productoEditado.getNomprod());

        // Establecer fondo blanco para el diálogo
        dialog.getDialogPane().setStyle("-fx-background-color: white;");

        ButtonType btnGuardar = new ButtonType("Guardar Cambios", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        // Aplicar estilo a los botones
        Button guardarBtn = (Button) dialog.getDialogPane().lookupButton(btnGuardar);
        guardarBtn.setStyle("-fx-background-color: #DF0909; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");

        Button cancelarBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelarBtn.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");

        // Crear GridPane con fondo blanco
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setPadding(new javafx.geometry.Insets(25, 40, 20, 40));
        grid.setStyle("-fx-background-color: white;");

        // Crear campos de entrada
        TextField txtNombre = new TextField(productoEditado.getNomprod());
        txtNombre.setStyle("-fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");

        TextField txtPrecio = new TextField(String.valueOf(productoEditado.getPrecio()));
        txtPrecio.setStyle("-fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");

        TextField txtStock = new TextField(String.valueOf(productoEditado.getStock()));
        txtStock.setStyle("-fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");

        TextField txtVendidos = new TextField(String.valueOf(productoEditado.getVendidos()));
        txtVendidos.setStyle("-fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");

        TextArea txtDescripcion = new TextArea(productoEditado.getDesc());
        txtDescripcion.setPrefRowCount(3);
        txtDescripcion.setStyle("-fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");

        ComboBox<String> cbCategoria = new ComboBox<>();
        cbCategoria.getItems().addAll("Maquillaje", "Cuidado Facial", "Accesorios", "Otros");
        cbCategoria.setValue(productoEditado.getCategoria());
        cbCategoria.setStyle("-fx-font-size: 14px; -fx-padding: 8; -fx-background-radius: 5;");

        // Crear labels con letras más gruesas
        Label lblNombre = new Label("Nombre:");
        lblNombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label lblPrecio = new Label("Precio:");
        lblPrecio.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label lblCategoria = new Label("Categoría:");
        lblCategoria.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label lblStock = new Label("Stock:");
        lblStock.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label lblVendidos = new Label("Vendidos:");
        lblVendidos.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label lblDescripcion = new Label("Descripción:");
        lblDescripcion.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Agregar elementos al grid
        grid.add(lblNombre, 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(lblPrecio, 0, 1);
        grid.add(txtPrecio, 1, 1);
        grid.add(lblCategoria, 0, 2);
        grid.add(cbCategoria, 1, 2);
        grid.add(lblStock, 0, 3);
        grid.add(txtStock, 1, 3);
        grid.add(lblVendidos, 0, 4);
        grid.add(txtVendidos, 1, 4);
        grid.add(lblDescripcion, 0, 5);
        grid.add(txtDescripcion, 1, 5);

        // Hacer que la descripción ocupe 2 filas
        GridPane.setRowSpan(txtDescripcion, 2);
        GridPane.setColumnSpan(txtDescripcion, 1);

        dialog.getDialogPane().setContent(grid);

        // Forzar que el diálogo mantenga el foco
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(500);

        // Configurar resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    System.out.println("=== VALIDANDO DATOS ===");

                    // Validaciones
                    String nuevoNombre = txtNombre.getText().trim();
                    String precioStr = txtPrecio.getText().trim();
                    String stockStr = txtStock.getText().trim();
                    String vendidosStr = txtVendidos.getText().trim();

                    if (nuevoNombre.isEmpty()) {
                        mostrarAlerta("Error", "El nombre no puede estar vacío", Alert.AlertType.ERROR);
                        return null;
                    }

                    if (precioStr.isEmpty()) {
                        mostrarAlerta("Error", "El precio no puede estar vacío", Alert.AlertType.ERROR);
                        return null;
                    }

                    if (stockStr.isEmpty()) {
                        mostrarAlerta("Error", "El stock no puede estar vacío", Alert.AlertType.ERROR);
                        return null;
                    }

                    if (vendidosStr.isEmpty()) {
                        mostrarAlerta("Error", "Los vendidos no pueden estar vacíos", Alert.AlertType.ERROR);
                        return null;
                    }

                    float nuevoPrecio = Float.parseFloat(precioStr);
                    int nuevoStock = Integer.parseInt(stockStr);
                    int nuevosVendidos = Integer.parseInt(vendidosStr);

                    if (nuevoPrecio <= 0) {
                        mostrarAlerta("Error", "El precio debe ser mayor a 0", Alert.AlertType.ERROR);
                        return null;
                    }

                    if (nuevoStock < 0) {
                        mostrarAlerta("Error", "El stock no puede ser negativo", Alert.AlertType.ERROR);
                        return null;
                    }

                    if (nuevosVendidos < 0) {
                        mostrarAlerta("Error", "Los vendidos no pueden ser negativos", Alert.AlertType.ERROR);
                        return null;
                    }

                    System.out.println("Validaciones pasadas");
                    System.out.println("Nuevo nombre: " + nuevoNombre);
                    System.out.println("Nuevo precio: " + nuevoPrecio);
                    System.out.println("Nuevo stock: " + nuevoStock);
                    System.out.println("Nuevos vendidos: " + nuevosVendidos);

                    // 1. Verificar si el nombre ya existe (excepto para este producto)
                    boolean nombreDuplicado = false;
                    for (producto p : productos) {
                        if (p != productoEditado && p.getNomprod().equalsIgnoreCase(nuevoNombre)) {
                            nombreDuplicado = true;
                            break;
                        }
                    }

                    if (nombreDuplicado) {
                        mostrarAlerta("Error", "Ya existe otro producto con ese nombre", Alert.AlertType.ERROR);
                        return null;
                    }

                    // 2. Actualizar el producto en la lista Observable
                    System.out.println("Actualizando producto en lista Observable...");
                    productoEditado.setNomprod(nuevoNombre);
                    productoEditado.setPrecio(nuevoPrecio);
                    productoEditado.setCategoria(cbCategoria.getValue());
                    productoEditado.setStock(nuevoStock);
                    productoEditado.setVendidos(nuevosVendidos);
                    productoEditado.setDesc(txtDescripcion.getText());

                    System.out.println("Producto actualizado en lista Observable");

                    // 3. ACTUALIZAR EL INVENTARIO CORRECTAMENTE
                    System.out.println("Recreando inventario completo...");

                    listaInventario nuevoInventario = new listaInventario();
                    for (producto p : productos) {
                        nuevoInventario.agregar(p);
                    }
                    inventario = nuevoInventario;

                    System.out.println("Inventario recreado con " + productos.size() + " productos");

                    // 4. Guardar en JSON
                    System.out.println("Guardando en productos.json...");
                    boolean guardado = JsonManager.guardarInventario(inventario);

                    if (guardado) {
                        // 5. Actualizar tabla
                        tablaProductos.refresh();
                        System.out.println("Producto actualizado y guardado en JSON");

                        // 6. Mostrar mensaje de éxito
                        mostrarAlerta("Éxito",
                                "Producto actualizado correctamente\n\n"
                                + "Nombre: " + productoEditado.getNomprod() + "\n"
                                + "Precio: $" + String.format("%,.0f", productoEditado.getPrecio()) + "\n"
                                + "Categoría: " + productoEditado.getCategoria() + "\n"
                                + "Stock: " + productoEditado.getStock() + " unidades\n"
                                + "Vendidos: " + productoEditado.getVendidos() + "\n\n"
                                + "Cambios guardados en productos.json",
                                Alert.AlertType.INFORMATION);

                        // 7. Actualizar estadísticas si es necesario
                        actualizarEstadisticasRojas();

                        return productoEditado;

                    } else {
                        System.err.println("Error al guardar en JSON");
                        mostrarAlerta("Error",
                                "No se pudieron guardar los cambios en el archivo.\n"
                                + "Verifique que el archivo productos.json no esté bloqueado.",
                                Alert.AlertType.ERROR);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Error de formato numérico: " + e.getMessage());
                    mostrarAlerta("Error de Formato",
                            "Los siguientes campos deben ser números válidos:\n\n"
                            + "•Precio (ej: 45000)\n"
                            + "•Stock (ej: 50)\n"
                            + "•Vendidos (ej: 10)\n\n"
                            + "Use solo números sin símbolos.",
                            Alert.AlertType.ERROR);

                } catch (Exception e) {
                    System.err.println("Error inesperado: " + e.getMessage());
                    e.printStackTrace();
                    mostrarAlerta("Error Inesperado",
                            "Ocurrió un error al guardar los cambios:\n" + e.getMessage(),
                            Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        // Mostrar diálogo
        dialog.showAndWait();

        System.out.println("=== FIN EDICIÓN DE PRODUCTO ===");
    }

    private void eliminarProducto(producto producto) {
        // Confirmar eliminación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de eliminar este producto?");
        alert.setContentText(
                "Producto a eliminar:\n\n"
                + "Nombre: " + producto.getNomprod() + "\n"
                + "ID: " + producto.getIdprod() + "\n"
                + "Precio: $" + String.format("%,.0f", producto.getPrecio()) + "\n"
                + "Stock: " + producto.getStock() + " unidades\n"
                + "Vendidos: " + producto.getVendidos() + "\n\n"
                + "Esta acción no se puede deshacer."
        );

        // Estilizar la alerta
        alert.getDialogPane().setStyle("-fx-background-color: white;");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    System.out.println("=== ELIMINANDO PRODUCTO ===");
                    System.out.println("ID: " + producto.getIdprod());
                    System.out.println("Nombre: " + producto.getNomprod());

                    // 1. Eliminar de la lista observable
                    boolean eliminadoDeLista = productos.remove(producto);
                    System.out.println("Eliminado de lista observable: " + eliminadoDeLista);

                    // 2. Recrear inventario sin el producto eliminado
                    listaInventario nuevoInventario = new listaInventario();
                    for (producto p : productos) {
                        nuevoInventario.agregar(p);
                    }
                    inventario = nuevoInventario;
                    System.out.println("Inventario recreado con " + productos.size() + " productos");

                    // 3. Guardar cambios en productos.json
                    boolean guardadoInventario = JsonManager.guardarInventario(inventario);
                    System.out.println("Guardado en productos.json: " + guardadoInventario);

                    // 4. Opcional: Eliminar referencias del producto en prodXusu.json
                    // Solo si tienes un método para esto en JsonManager
                    eliminarReferenciasProducto(producto.getIdprod());

                    // 5. Actualizar interfaz
                    tablaProductos.refresh();
                    actualizarEstadisticasRojas();

                    // Mostrar mensaje de éxito detallado
                    mostrarAlerta("Producto Eliminado",
                            "El producto ha sido eliminado exitosamente:\n\n"
                            + "Nombre: " + producto.getNomprod() + "\n"
                            + "ID: " + producto.getIdprod() + "\n"
                            + "Stock restante: " + producto.getStock() + " unidades\n"
                            + "Vendidos totales: " + producto.getVendidos() + "\n\n"
                            + "Los cambios han sido guardados en productos.json",
                            Alert.AlertType.INFORMATION);

                } catch (Exception e) {
                    System.err.println("Error al eliminar producto: " + e.getMessage());
                    e.printStackTrace();

                    mostrarAlerta("Error",
                            "No se pudo eliminar el producto:\n" + e.getMessage(),
                            Alert.AlertType.ERROR);
                }
            }
        });
    }

// Método opcional para eliminar referencias del producto en prodXusu.json
    private void eliminarReferenciasProducto(String idProducto) {
        try {
            System.out.println("Buscando referencias del producto ID: " + idProducto + " en prodXusu.json");

            // 1. Cargar prodXusu.json actual
            listaProdXusu listaProdXusu = new listaProdXusu();
            listaProdXusu.setUsuarioActual("admin"); // O cualquier usuario

            boolean cargado = JsonManager.cargarProdXUsu(listaProdXusu);

            if (cargado) {
                // 2. Obtener todos los registros
                ArrayList<prodXusu> registros = listaProdXusu.toArrayList();
                int totalInicial = registros.size();

                // 3. Filtrar para eliminar los registros de este producto
                ArrayList<prodXusu> registrosFiltrados = new ArrayList<>();
                for (prodXusu registro : registros) {
                    if (!registro.getIdprod().equals(idProducto)) {
                        registrosFiltrados.add(registro);
                    }
                }

                // 4. Si hubo cambios, guardar
                if (registrosFiltrados.size() < totalInicial) {
                    int eliminados = totalInicial - registrosFiltrados.size();
                    System.out.println("Encontradas y eliminadas " + eliminados + " referencias del producto");

                    // Crear nueva lista con los registros filtrados
                    listaProdXusu nuevaLista = new listaProdXusu();
                    nuevaLista.setUsuarioActual("admin");

                    // Cargar los registros filtrados
                    nuevaLista.fromArrayList(registrosFiltrados);

                    // Guardar cambios
                    JsonManager.guardarProdXUsu(nuevaLista);
                    System.out.println("Referencias eliminadas de prodXusu.json");
                } else {
                    System.out.println("No se encontraron referencias del producto en prodXusu.json");
                }
            }

        } catch (Exception e) {
            System.err.println("Error al eliminar referencias del producto: " + e.getMessage());
            // No mostrar error al usuario, ya que esto es opcional
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
