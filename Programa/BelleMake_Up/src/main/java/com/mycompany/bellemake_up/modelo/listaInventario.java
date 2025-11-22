/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author yennf
 */
public class listaInventario {

    public nodo<producto> cab;

    public listaInventario() {
        cab = null;
    }

    public boolean getEsVacia() {
        return cab == null;
        //return cab==null?true:false;

    }

    public nodo<producto> crearNodo(producto prod) {
        return new nodo<>(prod);
    }

 
    public void agregar(producto prod) {
        nodo<producto> nuevo = crearNodo(prod);
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

  
    public int tamaño() {
        int contador = 0;
        nodo<producto> actual = cab;
        while (actual != null) {
            contador++;
            actual = actual.sig;
        }
        return contador;
    }


    public producto obtener(int index) {
        if (index < 0 || index >= tamaño()) {
            return null;
        }

        nodo<producto> actual = cab;
        for (int i = 0; i < index; i++) {
            actual = actual.sig;
        }
        return actual.info;
    }


public listaInventario buscarPorNombre(String nombre) {
    listaInventario resultados = new listaInventario();
    nodo<producto> actual = cab;

    while (actual != null) {
        if (actual.info.getNomprod().toLowerCase().contains(nombre.toLowerCase())) {
            resultados.agregar(actual.info);
        }
        actual = actual.sig;
    }
    return resultados;
}


public producto buscarPorId(String id) {
    nodo<producto> actual = cab;
    while (actual != null) {
        if (actual.info.getIdprod().equalsIgnoreCase(id)) {
            return actual.info;
        }
        actual = actual.sig;
    }
    return null;
}

    public void mostrar() {
        nodo<producto> actual = cab;
        System.out.println("=== INVENTARIO (" + tamaño() + " productos) ===");
        while (actual != null) {
            System.out.println(actual.info);
            actual = actual.sig;
        }
    }
}
