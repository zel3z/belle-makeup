/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author yennf
 */
public class listaUsuario {

    public nodo<usuario> cab;

    public listaUsuario() {
        cab = null;
    }

    public boolean getEsVacia() {
        return cab == null;
        //return cab==null?true:false;
    }

   // MÉTODO PARA AGREGAR USUARIOS AL FINAL
    public void agregar(usuario user) {
        nodo<usuario> nuevo = new nodo<>(user);
        if (getEsVacia()) {
            cab = nuevo;
        } else {
            nodo<usuario> actual = cab;
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
        nodo<usuario> actual = cab;
        while (actual != null) {
            contador++;
            actual = actual.sig;
        }
        return contador;
    }

    // MÉTODO PARA OBTENER ELEMENTO POR POSICIÓN
    public usuario obtener(int index) {
        if (index < 0 || index >= tamaño()) {
            return null;
        }
        
        nodo<usuario> actual = cab;
        for (int i = 0; i < index; i++) {
            actual = actual.sig;
        }
        return actual.info;
    }

    // MÉTODO PARA BUSCAR USUARIO POR EMAIL
    public usuario buscarPorEmail(String nombreU) {
        nodo<usuario> actual = cab;
        while (actual != null) {
            if (actual.info.getNombre().equals(nombreU)) {
                return actual.info;
            }
            actual = actual.sig;
        }
        return null;
    }
}
