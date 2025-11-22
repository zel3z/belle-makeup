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

    // MÉTODO PARA AGREGAR PRODUCTOS AL FINAL
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

    // MÉTODO PARA OBTENER TAMAÑO
    public int tamaño() {
        int contador = 0;
        nodo<producto> actual = cab;
        while (actual != null) {
            contador++;
            actual = actual.sig;
        }
        return contador;
    }

    // MÉTODO PARA OBTENER ELEMENTO POR POSICIÓN
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

    // MÉTODO PARA MOSTRAR TODOS LOS PRODUCTOS
    public void mostrar() {
        nodo<producto> actual = cab;
        while (actual != null) {
            System.out.println(actual.info);
            actual = actual.sig;
        }
    }
}
