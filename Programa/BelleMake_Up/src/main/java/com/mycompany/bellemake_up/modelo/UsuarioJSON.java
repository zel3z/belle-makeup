/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


import static modelo.GestorJSONBase.gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
/**
 *
 * @author yennf
 */
public class UsuarioJSON extends GestorJSONBase {
    private static final String RUTA_USUARIOS = "/baseDeDatos/usuarios.json";

    public UsuarioJSON() {
        crearArchivoSiNoExiste(RUTA_USUARIOS);
    }

    // Guardar lista de usuarios a JSON
    public void guardarUsuarios(listaUsuario listaUsuarios) {
        try {
            String json = convertirListaAJson(listaUsuarios);
            
            try (FileWriter writer = new FileWriter(RUTA_USUARIOS)) {
                writer.write(json);
                System.out.println("Usuarios guardados en JSON");
            }
        } catch (IOException e) {
            System.err.println("Error guardando usuarios: " + e.getMessage());
        }
    }

    // Cargar JSON a lista de usuarios
    public listaUsuario cargarUsuarios() {
        listaUsuario lista = new listaUsuario();
        
        try (FileReader reader = new FileReader(RUTA_USUARIOS)) {
            Type listType = new TypeToken<List<usuario>>(){}.getType();
            List<usuario> usuariosTemp = gson.fromJson(reader, listType);
            
            if (usuariosTemp != null) {
                for (usuario user : usuariosTemp) {
                    lista.agregar(user);
                }
            }
            System.out.println("Usuarios cargados: " + lista.tamaño()); // Cambiado a tamaño()
        } catch (IOException e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
        }
        
        return lista;
    }

    private String convertirListaAJson(listaUsuario lista) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        
        // Usar los métodos que creamos: tamaño() y obtener()
        for (int i = 0; i < lista.tamaño(); i++) { // Cambiado a tamaño()
            usuario user = lista.obtener(i); // Cambiado a obtener()
            if (user != null) {
                if (i > 0) jsonBuilder.append(",");
                jsonBuilder.append(gson.toJson(user));
            }
        }
        
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}
