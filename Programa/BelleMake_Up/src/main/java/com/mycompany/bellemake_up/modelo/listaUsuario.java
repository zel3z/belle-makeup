package org.example.belle_makeup.modelo;

import static com.mycompany.bellemake_up.BelleMake_Up.listaProdXUsu;
import static com.mycompany.bellemake_up.BelleMake_Up.listaUsuario;
import static com.mycompany.bellemake_up.BelleMake_Up.usuarioActual;
import com.mycompany.bellemake_up.controlador.JsonManager;
import java.util.ArrayList;
import java.util.Objects;
import modelo.nodo;
import modelo.usuario;

public class listaUsuario implements JsonSerializable<usuario> {

    public nodo<usuario> cab;

    public listaUsuario() {
        cab = null;
    }

    public boolean getEsVacia() {
        return cab == null;
        //return cab==null?true:false;
    }

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

    @Override
    public ArrayList<usuario> toArrayList() {
        ArrayList<usuario> lista = new ArrayList<>();
        nodo<usuario> actual = cab;

        while (actual != null) {
            lista.add(actual.info);
            actual = actual.sig;
        }
        return lista;
    }

    @Override
    public void fromArrayList(ArrayList<usuario> datos) {
        limpiar();
        for (usuario usuario : datos) {
            agregar(usuario);
        }
    }

    @Override
    public void limpiar() {
        cab = null;
    }

    /**
     * Validar credenciales login
     */
    public usuario validarLogin(String nombreUsuario, String contra) {
       nodo<usuario> actual = cab;

        while (actual != null) {
            usuario user = actual.info;
            if (user.getUsuario().equals(nombreUsuario) &&
                    Objects.equals(user.getContra(), contra)) {
                return user;
            }
            actual = actual.sig;
        } return null;
    }

    /**
     * Verifica si un nombre de usuario ya existe
     * @param nombreUsuario Nombre de usuario a verificar
     * @return true si existe, false si está disponible
     */
    public boolean existeUsuario(String nombreUsuario) {
        nodo<usuario> actual = cab;

        while (actual != null) {
            if (actual.info.getUsuario().equals(nombreUsuario)) {
                return true;
            }
            actual = actual.sig;
        }
        return false;
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
    
    public static boolean registrarUsuario(String usuario, String nombre,
                                           String contra, String correo, String rol) {
        if (listaUsuario.existeUsuario(usuario)) {
            return false;
        }
        usuario nuevo = new usuario(usuario, nombre, contra, correo, rol);
        listaUsuario.agregar(nuevo);
        JsonManager.guardarUsuarios(listaUsuario);
        return true;
    }
    
    public static boolean realizarLogin(String usuario, String contrasena) {
        usuario user = listaUsuario.validarLogin(usuario, contrasena);
        if (user != null) {
            usuarioActual = usuario;
            listaProdXUsu.setUsuarioActual(usuarioActual);
            JsonManager.cargarProdXUsu(listaProdXUsu);
            return true;
        }
        return false;
    }

}
