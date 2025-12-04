package com.mycompany.bellemake_up.modelo;

import java.util.ArrayList;

public class listaInventario implements JsonSerializable<producto> {

    public nodo<producto> cab;

    public listaInventario() {
        cab = null;
    }

    public boolean getEsVacia() {
        return cab == null;
        //return cab==null?true:false;
    }

    @Override
    public ArrayList<producto> toArrayList() { //pasar toda la lista enlazada a ArrayList para serializar a JSON
        ArrayList<producto> lista = new ArrayList<>(); //nuevo arraylist porq se reescribirá todo
        nodo<producto> actual = cab;

        while (actual != null) {
            lista.add(actual.info);
            actual = actual.sig;
        }
        return lista;
    }

    /**
     * Carga productos desde ArrayList deserializado de JSON
     * @param datos ArrayList de productos
     */
    @Override
    public void fromArrayList(ArrayList<producto> datos) {
        limpiar();
        for (producto producto : datos) {
            agregar(producto);
        }
    }

    @Override
    public void limpiar() {
        cab = null;
    }

    // para usar en prodXusu pues allá solo se maneja IDprod
    //ej cuando usuario accione un producto se atrapa el nombre en cierto componente
    //y se busca con este para guardar id en el nodo
    public String buscarIdPorNombre(String nombre) {
        nodo<producto> actual = cab;

        while (actual != null) {
            if (actual.info.getNomprod().equalsIgnoreCase(nombre)) {
                return actual.info.getIdprod();
            }
            actual = actual.sig;
        }
        return null;
    }

    /**
     * Reduce el stock de un producto después de una compra
     * @param idprod ID del producto
     * @param cantidad Cantidad a reducir
     * @return true si se redujo exitosamente, false si no hay suficiente stock
     */
    public boolean reducirStock(String idprod, int cantidad) {
        nodo<producto> actual = cab;

        while (actual != null) {
            if (actual.info.getIdprod().equals(idprod)) {
                int stockActual = actual.info.getStock();

                if (stockActual >= cantidad) {
                    actual.info.setStock(stockActual - cantidad);
                    actual.info.setVendidos(actual.info.getVendidos() + cantidad);
                    return true;
                } else {
                    return false; // No hay suficiente stock
                }
            }
            actual = actual.sig;
        }
        return false; // Producto no encontrado
    }

    public void agregar(producto prod) {
        nodo<producto> nuevo = new nodo<>(prod);
        if (getEsVacia()) {
            cab = nuevo;
        } else {
            nodo<producto> actual = cab;
            while (actual.sig != null) {
                actual = actual.sig;
            }
            actual.sig = nuevo;
            nuevo.ant = actual;
        }
    }

    public boolean actualizarProducto(producto productoActualizado) {
        nodo<producto> actual = cab;

        while (actual != null) {
            if (actual.info.getIdprod().equals(productoActualizado.getIdprod())) {
                // Actualizar todos los campos del producto
                actual.info.setNomprod(productoActualizado.getNomprod());
                actual.info.setPrecio(productoActualizado.getPrecio());
                actual.info.setCategoria(productoActualizado.getCategoria());
                actual.info.setStock(productoActualizado.getStock());
                actual.info.setVendidos(productoActualizado.getVendidos());
                actual.info.setDesc(productoActualizado.getDesc());
                actual.info.setRutaImagen(productoActualizado.getRutaImagen());
                return true;
            }
            actual = actual.sig;
        }
        return false; // Producto no encontrado
    }
}
