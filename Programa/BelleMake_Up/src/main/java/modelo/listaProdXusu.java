/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author yennf
 */
public class listaProdXusu {

    public nodo<prodXusu> cab;

    public listaProdXusu() {
        cab = null;
    }

    public boolean getEsVacia() {
        return cab == null;
        //return cab==null?true:false;
    }

    // MÉTODO PARA AGREGAR ELEMENTOS AL FINAL
    public void agregar(prodXusu item) {
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

    // MÉTODO PARA OBTENER TAMAÑO
    public int tamaño() {
        int contador = 0;
        nodo<prodXusu> actual = cab;
        while (actual != null) {
            contador++;
            actual = actual.sig;
        }
        return contador;
    }

    // MÉTODO PARA OBTENER ELEMENTO POR POSICIÓN
    public prodXusu obtener(int index) {
        if (index < 0 || index >= tamaño()) {
            return null;
        }

        nodo<prodXusu> actual = cab;
        for (int i = 0; i < index; i++) {
            actual = actual.sig;
        }
        return actual.info;
    }
}
