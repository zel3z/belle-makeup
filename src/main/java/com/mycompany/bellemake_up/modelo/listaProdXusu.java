package com.mycompany.bellemake_up.modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class listaProdXusu implements JsonSerializable<prodXusu> {

    public nodo<prodXusu> cab;
    private String usuarioActual;

    public listaProdXusu() {
        cab = null;
        this.usuarioActual = null;
    }

    public boolean getEsVacia() {
        return cab == null;
        //return cab==null?true:false;
    }

    public void setUsuarioActual(String usuario) {
        this.usuarioActual = usuario;
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void insertar(prodXusu item) {
        nodo<prodXusu> nuevo = new nodo<>(item);
        if (getEsVacia()) {
            cab = nuevo;
        } else {
            nodo<prodXusu> actual = cab;
            while (actual.sig != null) {
                actual = actual.sig;
            }
            actual.sig = nuevo;
            nuevo.ant = actual;
        }
    }

    @Override
    public ArrayList<prodXusu> toArrayList() {
        ArrayList<prodXusu> lista = new ArrayList<>();
        nodo<prodXusu> actual = cab;

        while (actual != null) {
            lista.add(actual.info);
            actual = actual.sig;
        }

        return lista;
    }

    /**
     * Carga datos desde ArrayList y filtra SOLO los del usuario actual
     *
     * @param datos ArrayList completo con datos de todos los usuarios
     */
    @Override
    public void fromArrayList(ArrayList<prodXusu> datos) {
        limpiar();

        if (usuarioActual == null) {
            return; // No hay usuario para filtrar
        }

        for (prodXusu prodxusu : datos) {
            if (prodxusu.getUsuario().equals(usuarioActual)) {
                insertar(prodxusu);
            }
        }
    }

    /**
     * Limpia toda la lista
     */
    @Override
    public void limpiar() {
        cab = null;
    }

    /**
     * Busca un nodo específico por usuario e ID de producto
     *
     * @param usuario Nombre del usuario
     * @param idprod ID del producto
     * @return Nodo encontrado o null si no existe
     */
    public nodo<prodXusu> buscarNodo(String usuario, String idprod) {
        nodo<prodXusu> actual = cab;

        while (actual != null) {
            prodXusu dato = actual.info;
            if (dato.getUsuario().equals(usuario)
                    && dato.getIdprod().equals(idprod)) {
                return actual;
            }
            actual = actual.sig;
        }

        return null;
    }

    /**
     * Agrega o actualiza el estado de favorito de un producto
     *
     * @param usuario Nombre del usuario
     * @param idprod ID del producto
     * @return true si quedó marcado como favorito, false si se desmarcó
     */
    public boolean agregarOActualizarFavorito(String usuario, String idprod) {
        nodo<prodXusu> nodo = buscarNodo(usuario, idprod);

        if (nodo == null) {
            prodXusu nuevo = new prodXusu();
            nuevo.setUsuario(usuario);
            nuevo.setIdprod(idprod);
            nuevo.setFavorito(true);
            nuevo.setCarrito(false);
            nuevo.setComprado(null);
            nuevo.setCantiComprado(0);

            insertar(nuevo);
            return true;
        } else {
            boolean nuevoEstado = !nodo.info.isFavorito();
            nodo.info.setFavorito(nuevoEstado);
            return nuevoEstado;
        }
    }

    /**
     * Agrega o actualiza un producto en el carrito con cantidad específica
     *
     * @param usuario Nombre del usuario
     * @param idprod ID del producto
     * @param cantidad Cantidad en el carrito
     * @return true si se agregó/actualizó exitosamente
     */
    public boolean agregarOActualizarCarritoConCantidad(String usuario, String idprod, int cantidad) {
        nodo<prodXusu> nodo = buscarNodo(usuario, idprod);

        if (nodo == null) {
            // Crear nuevo registro
            prodXusu nuevo = new prodXusu();
            nuevo.setUsuario(usuario);
            nuevo.setIdprod(idprod);
            nuevo.setFavorito(false);
            nuevo.setCarrito(true);
            nuevo.setComprado(null);
            nuevo.setCantiComprado(cantidad);

            insertar(nuevo);
            return true;
        } else {
            // Actualizar registro existente
            prodXusu dato = nodo.info;
            dato.setCarrito(true);
            dato.setCantiComprado(cantidad);
            return true;
        }
    }

    /**
     * Elimina un producto del carrito
     *
     * @param usuario Nombre del usuario
     * @param idprod ID del producto
     * @return true si se eliminó exitosamente
     */
    public boolean eliminarDelCarrito(String usuario, String idprod) {
        nodo<prodXusu> nodo = buscarNodo(usuario, idprod);

        if (nodo != null) {
            nodo.info.setCarrito(false);
            nodo.info.setCantiComprado(0);
            return true;
        }
        return false;
    }

    /**
     * Obtiene todos los productos en carrito del usuario actual con sus
     * cantidades
     *
     * @return ArrayList de prodXusu en carrito
     */
    public ArrayList<prodXusu> obtenerCarritoConCantidades() {
        ArrayList<prodXusu> carrito = new ArrayList<>();
        nodo<prodXusu> actual = cab;

        while (actual != null) {
            if (actual.info.isCarrito() && actual.info.getCantiComprado() > 0) {
                carrito.add(actual.info);
            }
            actual = actual.sig;
        }
        return carrito;
    }

    /**
     * Registra una compra de producto
     *
     * @param usuario Nombre del usuario
     * @param idprod ID del producto
     * @param cantidad Cantidad comprada
     */
    public void registrarCompra(String usuario, String idprod, int cantidad) {
        // SIEMPRE crear un NUEVO registro para cada compra
        // Esto permite tener historial completo de compras del mismo producto

        prodXusu nuevoCompra = new prodXusu();
        nuevoCompra.setUsuario(usuario);
        nuevoCompra.setIdprod(idprod);
        nuevoCompra.setFavorito(false);
        nuevoCompra.setCarrito(false);
        nuevoCompra.setComprado(LocalDate.now());
        nuevoCompra.setCantiComprado(cantidad);

        insertar(nuevoCompra);

        System.out.println(" NUEVA compra registrada: " + idprod
                + "" + cantidad
                + " - Fecha: " + LocalDate.now());

        // ✅ También quitar del carrito si el producto estaba ahí
        nodo<prodXusu> nodoExistente = buscarNodo(usuario, idprod);
        if (nodoExistente != null && nodoExistente.info.isCarrito()) {
            nodoExistente.info.setCarrito(false);
            System.out.println("Producto quitado del carrito después de comprar");
        }
    }

    /**
     * Obtiene todos los productos en carrito del usuario actual
     *
     * @return ArrayList de productos en carrito
     */
    public ArrayList<prodXusu> obtenerCarrito() {
        ArrayList<prodXusu> carrito = new ArrayList<>();
        nodo<prodXusu> actual = cab;

        while (actual != null) {
            if (actual.info.isCarrito()) {
                carrito.add(actual.info);
            }
            actual = actual.sig;
        }

        return carrito;
    }

    /**
     * Obtiene todos los productos favoritos del usuario actual
     *
     * @return ArrayList de productos favoritos
     */
    public ArrayList<prodXusu> obtenerFavoritos() {
        ArrayList<prodXusu> favoritos = new ArrayList<>();
        nodo<prodXusu> actual = cab;

        while (actual != null) {
            if (actual.info.isFavorito()) {
                favoritos.add(actual.info);
            }
            actual = actual.sig;
        }
        return favoritos;
    }

    /**
     * Obtiene todas las compras del usuario actual
     *
     * @return ArrayList de productos comprados
     */
    public ArrayList<prodXusu> obtenerCompras() {
        ArrayList<prodXusu> compras = new ArrayList<>();
        nodo<prodXusu> actual = cab;

        while (actual != null) {
            if (actual.info.getComprado() != null) {
                compras.add(actual.info);
            }
            actual = actual.sig;
        }

        return compras;
    }

    /**
     * Verifica si un producto está en favoritos
     *
     * @param idprod ID del producto
     * @return true si está en favoritos
     */
    public boolean esFavorito(String idprod) {
        nodo<prodXusu> nodo = buscarNodo(usuarioActual, idprod);
        return nodo != null && nodo.info.isFavorito();
    }

    /**
     * Verifica si un producto está en carrito
     *
     * @param idprod ID del producto
     * @return true si está en carrito
     */
    public boolean estaEnCarrito(String idprod) {
        nodo<prodXusu> nodo = buscarNodo(usuarioActual, idprod);
        return nodo != null && nodo.info.isCarrito();
    }

    public int tamaño() {
        int contador = 0;
        nodo<prodXusu> actual = cab;
        while (actual != null) {
            contador++;
            actual = actual.sig;
        }
        return contador;
    }

}
