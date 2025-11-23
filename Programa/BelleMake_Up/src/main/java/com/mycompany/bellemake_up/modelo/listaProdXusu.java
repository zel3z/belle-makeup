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

    public nodo<prodXusu> crearNodo(prodXusu pxu) {
        return new nodo<>(pxu);
    }


    public void agregar(prodXusu pxu) {
        nodo<prodXusu> nuevo = crearNodo(pxu);
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


    public int tamaño() {
        int contador = 0;
        nodo<prodXusu> actual = cab;
        while (actual != null) {
            contador++;
            actual = actual.sig;
        }
        return contador;
    }

 
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
