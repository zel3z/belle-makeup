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


    public nodo<usuario> crearNodo(usuario user) {
        return new nodo<>(user);
    }

 
    public void agregar(usuario user) {
        nodo<usuario> nuevo = crearNodo(user);
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

 
    public int tamaño() {
        int contador = 0;
        nodo<usuario> actual = cab;
        while (actual != null) {
            contador++;
            actual = actual.sig;
        }
        return contador;
    }

   
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

  
    public usuario buscarPorId(String id) {
        nodo<usuario> actual = cab;
        while (actual != null) {
            if (actual.info.getUsuario().equalsIgnoreCase(id)) {
                return actual.info;
            }
            actual = actual.sig;
        }
        return null;
    }


    public boolean validarLogin(String usuario, String password) {
        usuario user = buscarPorId(usuario);
        return user != null && user.getContra().equals(password);
    }
}
