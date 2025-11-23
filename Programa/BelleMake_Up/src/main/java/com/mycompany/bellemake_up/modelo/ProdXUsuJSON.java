/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.*;
/**
 *
 * @author yennf
 */
public class ProdXUsuJSON extends GestorJSONBase {
    private static final String RUTA_PROD_X_USU = "/baseDeDatos/prodXusu.json";

    public ProdXUsuJSON() {
        crearArchivoSiNoExiste(RUTA_PROD_X_USU);
    }

    public void guardarProdXUsu(listaProdXusu listaProdXUsu) {
        try {
            String json = convertirListaAJson(listaProdXUsu);
            
            try (FileWriter writer = new FileWriter(RUTA_PROD_X_USU)) {
                writer.write(json);
                System.out.println("ProdXUsu guardados: " + listaProdXUsu.tamaño());
            }
        } catch (IOException e) {
            System.err.println("Error guardando prodXusu: " + e.getMessage());
        }
    }

    public listaProdXusu cargarProdXUsu() {
        listaProdXusu lista = new listaProdXusu();
        
        try (FileReader reader = new FileReader(RUTA_PROD_X_USU)) {
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
                    if (!objStr.startsWith("{")) objStr = "{" + objStr;
                    if (!objStr.endsWith("}")) objStr = objStr + "}";
                    
                    prodXusu pxu = gson.fromJson(objStr, prodXusu.class);
                    if (pxu != null) {
                        lista.agregar(pxu);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando prodXusu: " + e.getMessage());
        }
        
        return lista;
    }

    private String convertirListaAJson(listaProdXusu lista) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        
        for (int i = 0; i < lista.tamaño(); i++) {
            prodXusu pxu = lista.obtener(i);
            if (pxu != null) {
                if (i > 0) jsonBuilder.append(",");
                jsonBuilder.append(gson.toJson(pxu));
            }
        }
        
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}
