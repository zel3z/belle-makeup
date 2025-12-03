package com.mycompany.bellemake_up.controlador;

import com.mycompany.bellemake_up.BelleMake_Up;
import com.mycompany.bellemake_up.modelo.producto;
import com.mycompany.bellemake_up.modelo.prodXusu;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CheckoutvistaController implements Initializable {

    @FXML
    private Label lblTotalPagar;
    @FXML
    private Label lblResumenProductos;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblEnvio;
    @FXML
    private Label lblTotal;
    @FXML
    private VBox contenedorResumen;

    @FXML
    private TextField txtNumeroTarjeta;
    @FXML
    private TextField txtCCV;
    @FXML
    private TextField txtFechaExpiracion;
    @FXML
    private TextField txtNombreCompleto;
    @FXML
    private TextField txtCiudad;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtCodigoPostal;
    @FXML
    private TextField txtTelefono;

    private double totalCompra;
    private boolean esCompraDirecta = false; // TRUE si viene desde detalles, FALSE si viene desde carrito
    private producto productoDirecto = null; // Producto si es compra directa
    private int cantidadDirecta = 0; // Cantidad si es compra directa
    private Map<producto, Integer> carritoDirecto = new HashMap<>(); // Carrito temporal para compra directa

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inicializando CheckoutController...");
        
        // Verificar si es una compra directa desde detalles
        if (BelleMake_Up.compraDirectaProducto != null) {
            System.out.println("✓ Checkout detectó compra directa desde detalles");
            configurarCompraDirecta();
        } else {
            System.out.println("✓ Checkout para compra normal desde carrito");
            configurarCompraDesdeCarrito();
        }
    }

    private void configurarCompraDirecta() {
        // Marcar como compra directa
        esCompraDirecta = true;
        productoDirecto = BelleMake_Up.compraDirectaProducto;
        cantidadDirecta = BelleMake_Up.compraDirectaCantidad;
        
        // Crear carrito temporal solo con este producto
        carritoDirecto.put(productoDirecto, cantidadDirecta);
        
        System.out.println("Compra directa configurada: " + 
                          productoDirecto.getNomprod() + " x" + cantidadDirecta);
        
        // Calcular resumen
        calcularResumenCompraDirecta();
        
        // Limpiar las variables estáticas después de usarlas
        BelleMake_Up.compraDirectaProducto = null;
        BelleMake_Up.compraDirectaCantidad = 0;
    }

    private void configurarCompraDesdeCarrito() {
        esCompraDirecta = false;
        calcularResumenCompraDesdeCarrito();
    }

    private void calcularResumenCompraDirecta() {
        double precioProducto = productoDirecto.getPrecio();
        double subtotal = precioProducto * cantidadDirecta;
        double envio = 10000;
        totalCompra = subtotal + envio;

        lblTotalPagar.setText("Total a pagar: $" + String.format("%,.0f", totalCompra));
        lblSubtotal.setText("Subtotal: $" + String.format("%,.0f", subtotal));
        lblEnvio.setText("Envío: $" + String.format("%,.0f", envio));
        lblTotal.setText("Total: $" + String.format("%,.0f", totalCompra));

        // Resumen del producto único
        String resumen = "• " + productoDirecto.getNomprod() + 
                        " x" + cantidadDirecta + 
                        ": $" + String.format("%,.0f", subtotal) + "\n" +
                        "Precio unitario: $" + String.format("%,.0f", precioProducto);
        
        lblResumenProductos.setText(resumen);

        System.out.println("📊 Resumen COMPRA DIRECTA - Producto: " + productoDirecto.getNomprod() + 
                         ", Cantidad: " + cantidadDirecta + ", Total: $" + totalCompra);
    }

    private void calcularResumenCompraDesdeCarrito() {
        double subtotal = CarritovistaController.getTotalCarrito();
        double envio = 10000;
        totalCompra = subtotal + envio;

        lblTotalPagar.setText("Total a pagar: $" + String.format("%,.0f", totalCompra));
        lblSubtotal.setText("Subtotal: $" + String.format("%,.0f", subtotal));
        lblEnvio.setText("Envío: $" + String.format("%,.0f", envio));
        lblTotal.setText("Total: $" + String.format("%,.0f", totalCompra));

        // Generar resumen de productos desde el carrito
        StringBuilder resumen = new StringBuilder();
        for (Map.Entry<producto, Integer> entry : CarritovistaController.getCarrito().entrySet()) {
            producto p = entry.getKey();
            int cantidad = entry.getValue();
            resumen.append("• ").append(p.getNomprod())
                    .append(" x").append(cantidad)
                    .append(": $").append(String.format("%,.0f", p.getPrecio() * cantidad))
                    .append("\n");
        }

        lblResumenProductos.setText(resumen.toString());

        System.out.println("📊 Resumen desde CARRITO - Total: $" + totalCompra);
    }

    @FXML
    private void confirmarCompra() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }

        try {
            if (esCompraDirecta) {
                // Registrar solo la compra directa
                registrarCompraDirecta();
            } else {
                // Registrar todo el carrito
                registrarComprasDesdeCarrito();
            }

            // Limpiar según el tipo de compra
            if (esCompraDirecta) {
                // No limpiar carrito real, solo el temporal
                carritoDirecto.clear();
            } else {
                CarritovistaController.limpiarCarrito();
            }

            // Mostrar mensaje de éxito
            mostrarAlerta("¡Compra exitosa!", 
                "Tu pedido ha sido procesado correctamente.\n" +
                "Número de orden: #" + System.currentTimeMillis());

            // Abrir historial de compras
            HistorialComprasvistaController.abrirHistorial();

            // Cerrar ventana de checkout
            cerrarVentana();

        } catch (Exception e) {
            System.err.println("Error al procesar compra: " + e.getMessage());
            mostrarAlerta("Error", "No se pudo procesar la compra: " + e.getMessage());
        }
    }

    private void registrarCompraDirecta() {
        if (BelleMake_Up.usuarioActual != null && BelleMake_Up.listaProdXusu != null) {
            System.out.println("Registrando COMPRA DIRECTA: " + 
                             productoDirecto.getNomprod() + " x" + cantidadDirecta);

            // ✅ Registrar solo este producto (NO agregar al carrito)
            prodXusu compra = new prodXusu();
            compra.setUsuario(BelleMake_Up.usuarioActual);
            compra.setIdprod(productoDirecto.getIdprod());
            compra.setFavorito(false);
            compra.setCarrito(false); // IMPORTANTE: NO está en carrito
            compra.setComprado(LocalDate.now());
            compra.setCantiComprado(cantidadDirecta);

            // Insertar la compra directamente
            BelleMake_Up.listaProdXusu.insertar(compra);

            // Reducir stock
            reducirStockProducto(productoDirecto.getIdprod(), cantidadDirecta);

            // Guardar cambios
            JsonManager.guardarProdXUsu(BelleMake_Up.listaProdXusu);
            JsonManager.guardarInventario(BelleMake_Up.listaInventario);

            System.out.println("✅ Compra directa registrada: " + productoDirecto.getNomprod());
        }
    }

    private void registrarComprasDesdeCarrito() {
        if (BelleMake_Up.usuarioActual != null && BelleMake_Up.listaProdXusu != null) {
            Map<producto, Integer> carrito = CarritovistaController.getCarrito();
            System.out.println("Registrando " + carrito.size() + " productos del carrito...");

            for (Map.Entry<producto, Integer> entry : carrito.entrySet()) {
                producto p = entry.getKey();
                int cantidad = entry.getValue();

                // Usar el método registrarCompra
                BelleMake_Up.listaProdXusu.registrarCompra(
                        BelleMake_Up.usuarioActual,
                        p.getIdprod(),
                        cantidad
                );

                // Reducir stock
                reducirStockProducto(p.getIdprod(), cantidad);
            }

            // Guardar cambios
            JsonManager.guardarProdXUsu(BelleMake_Up.listaProdXusu);
            JsonManager.guardarInventario(BelleMake_Up.listaInventario);

            System.out.println("✅ " + carrito.size() + " compras registradas desde carrito");
        }
    }

    private void reducirStockProducto(String idProducto, int cantidad) {
        com.mycompany.bellemake_up.modelo.nodo<producto> actual = BelleMake_Up.listaInventario.cab;
        while (actual != null) {
            if (actual.info.getIdprod().equals(idProducto)) {
                int nuevoStock = actual.info.getStock() - cantidad;
                if (nuevoStock < 0) nuevoStock = 0;
                actual.info.setStock(nuevoStock);
                System.out.println("Stock reducido: " + idProducto + 
                                 " - Nuevo stock: " + nuevoStock);
                break;
            }
            actual = actual.sig;
        }
    }

    private boolean validarCampos() {
        // Validar tarjeta
        if (txtNumeroTarjeta.getText().trim().isEmpty()
                || !txtNumeroTarjeta.getText().matches("\\d{16}|\\d{4} \\d{4} \\d{4} \\d{4}")) {
            mostrarAlerta("Error", "Número de tarjeta inválido (16 dígitos requeridos)");
            return false;
        }

        if (txtCCV.getText().trim().isEmpty() || !txtCCV.getText().matches("\\d{3,4}")) {
            mostrarAlerta("Error", "CCV inválido (3-4 dígitos requeridos)");
            return false;
        }

        if (txtFechaExpiracion.getText().trim().isEmpty()
                || !txtFechaExpiracion.getText().matches("\\d{2}/\\d{2}")) {
            mostrarAlerta("Error", "Fecha de expiración inválida (Formato: MM/AA)");
            return false;
        }

        // Validar información personal
        if (txtNombreCompleto.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "Nombre completo requerido");
            return false;
        }

        if (txtDireccion.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "Dirección requerida");
            return false;
        }

        return true;
    }

    @FXML
    private void cancelarCompra() {
        // Limpiar variables de compra directa si se cancela
        if (esCompraDirecta) {
            BelleMake_Up.compraDirectaProducto = null;
            BelleMake_Up.compraDirectaCantidad = 0;
        }
        
        mostrarAlerta("Compra cancelada", "La compra ha sido cancelada");
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) lblTotalPagar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}