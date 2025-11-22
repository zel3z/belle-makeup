/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
/**
 *
 * @author yennf
 */
public class GestorJSONBase {

    protected static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected void crearArchivoSiNoExiste(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            try {
                archivo.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(archivo);
                writer.write("[]");
                writer.close();
                System.out.println("Archivo creado: " + ruta);
            } catch (IOException e) {
                System.err.println("Error creando archivo: " + ruta);
            }
        }
    }
}
