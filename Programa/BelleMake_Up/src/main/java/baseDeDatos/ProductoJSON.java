/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baseDeDatos;

import modelo.producto;
import modelo.listaInventario;
import java.io.*;
/**
 *
 * @author yennf
 */
public class ProductoJSON extends GestorJSONBase {

    private static final String RUTA_PRODUCTOS = "baseDeDatos/productos.json";

    public ProductoJSON() {
        crearArchivoSiNoExiste(RUTA_PRODUCTOS);
    }

    public void guardarProductos(listaInventario listaProductos) {
        try {
            String json = convertirListaAJson(listaProductos);

            try (FileWriter writer = new FileWriter(RUTA_PRODUCTOS)) {
                writer.write(json);
                System.out.println("Productos guardados: " + listaProductos.tamaño());
            }
        } catch (IOException e) {
            System.err.println("Error guardando productos: " + e.getMessage());
        }
    }

    public listaInventario cargarProductos() {
        listaInventario lista = new listaInventario();

        try (FileReader reader = new FileReader(RUTA_PRODUCTOS)) {
            StringBuilder jsonContent = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                jsonContent.append((char) character);
            }

            String content = jsonContent.toString().trim();
            if (content.length() > 2) {
                String jsonObjects = content.substring(1, content.length() - 1);
                String[] objects = jsonObjects.split("},\\s*\\{");

                for (int i = 0; i < objects.length; i++) {
                    String objStr = objects[i];
                    if (!objStr.startsWith("{")) {
                        objStr = "{" + objStr;
                    }
                    if (!objStr.endsWith("}")) {
                        objStr = objStr + "}";
                    }

                    producto prod = gson.fromJson(objStr, producto.class);
                    if (prod != null) {
                        lista.agregar(prod);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando productos: " + e.getMessage());
        }

        return lista;
    }

    private String convertirListaAJson(listaInventario lista) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");

        for (int i = 0; i < lista.tamaño(); i++) {
            producto prod = lista.obtener(i);
            if (prod != null) {
                if (i > 0) {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append(gson.toJson(prod));
            }
        }

        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

}
