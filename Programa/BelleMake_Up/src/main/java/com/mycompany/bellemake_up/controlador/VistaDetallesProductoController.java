package com.mycompany.bellemake_up.controlador;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.producto;

public class VistaDetallesProductoController {

    @FXML private ImageView imgProducto;
    @FXML private Label lblNombre;
    @FXML private Label lblPrecio;
    @FXML private TextArea txtDescripcion;

    @FXML private Button btnIzquierda;
    @FXML private Button btnDerecha;

    @FXML private Button btnMenos;
    @FXML private Button btnMas;

    @FXML private Label lblCantidad;

    @FXML private Button btnFavoritos;
    @FXML private Button btnComprar;
    @FXML private Button btnCarrito;

    private int cantidad = 1;
    private producto prod;

    @FXML
    public void initialize() {
        lblCantidad.setText("1");

        // Aumentar cantidad
        btnMas.setOnAction(e -> {
            if (prod != null && cantidad < prod.getStock()) {
                cantidad++;
                lblCantidad.setText(String.valueOf(cantidad));
            }
        });

        // Disminuir cantidad
        btnMenos.setOnAction(e -> {
            if (cantidad > 1) {
                cantidad--;
                lblCantidad.setText(String.valueOf(cantidad));
            }
        });

        btnCarrito.setOnAction(e -> {
            System.out.println("Añadido al carrito: " + prod.getNomprod());
        });

        btnComprar.setOnAction(e -> {
            System.out.println("Compra iniciada: " + prod.getNomprod());
        });

        btnFavoritos.setOnAction(e -> {
            System.out.println("Añadido a favoritos: " + prod.getNomprod());
        });
    }

    /** Recibir el producto desde el catálogo */
    public void setProducto(producto p) {
        this.prod = p;

        lblNombre.setText(p.getNomprod());
        lblPrecio.setText("$" + p.getPrecio());
        txtDescripcion.setText(p.getDesc());

        try {
            if (p.getRutaImagen() != null) {
                imgProducto.setImage(new Image("file:" + p.getRutaImagen()));
            }
        } catch (Exception e) {
            System.out.println("Error cargando imagen: " + e.getMessage());
        }
    }
}
