package com.mycompany.bellemake_up.controlador;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.belle_makeup.modelo.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import modelo.prodXusu;
import modelo.producto;
import modelo.usuario;

public class JsonManager {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
            .create();

    // Ruta base para los archivos JSON (ajusta según tu estructura)
    private static final String JSON_PATH = "src/main/java/org/example/belle_makeup/basededatos/";

    // Nombres de archivos
    private static final String INVENTARIO_FILE = "productos.json";
    private static final String USUARIOS_FILE = "usuarios.json";
    private static final String PRODXUSU_FILE = "prodXusu.json";

    // ==================== MÉTODOS PARA INVENTARIO ====================

    /**
     * Carga el inventario desde JSON a la lista
     * @param listaInventario Lista donde cargar los datos
     * @return true si se cargó exitosamente
     */
    public static boolean cargarInventario(listaInventario listaInventario) {
        try {
            File archivo = new File(JSON_PATH + INVENTARIO_FILE);

            // Si el archivo no existe, crear uno vacío
            if (!archivo.exists()) {
                System.out.println("Archivo " + INVENTARIO_FILE + " no existe. Creando uno vacío...");
                guardarInventario(listaInventario);
                return true;
            }

            // Leer archivo
            try (Reader reader = new FileReader(archivo, StandardCharsets.UTF_8)) {
                Type tipoLista = new TypeToken<ArrayList<producto>>(){}.getType();
                ArrayList<producto> productos = gson.fromJson(reader, tipoLista);

                if (productos != null) {
                    listaInventario.fromArrayList(productos);
                    System.out.println("Inventario cargado: " + productos.size() + " productos.");
                    return true;
                } else {
                    System.out.println("Archivo vacío, inventario inicializado sin productos.");
                    return true;
                }
            }

        } catch (IOException e) {
            System.err.println("Error al cargar inventario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Guarda el inventario desde la lista a JSON
     * @param listaInventario Lista con los datos a guardar
     * @return true si se guardó exitosamente
     */
    public static boolean guardarInventario(listaInventario listaInventario) {
        try {
            File archivo = new File(JSON_PATH + INVENTARIO_FILE);
            archivo.getParentFile().mkdirs(); // Crear carpetas si no existen

            ArrayList<producto> productos = listaInventario.toArrayList();

            try (Writer writer = new FileWriter(archivo, StandardCharsets.UTF_8)) {
                gson.toJson(productos, writer);
                System.out.println("Inventario guardado: " + productos.size() + " productos.");
                return true;
            }

        } catch (IOException e) {
            System.err.println("Error al guardar inventario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ==================== MÉTODOS PARA USUARIOS ====================

    /**
     * Carga los usuarios desde JSON a la lista
     * @param listaUsuario Lista donde cargar los datos
     * @return true si se cargó exitosamente
     */
    public static boolean cargarUsuarios(listaUsuario listaUsuario) {
        try {
            File archivo = new File(JSON_PATH + USUARIOS_FILE);

            // Si el archivo no existe, crear uno vacío
            if (!archivo.exists()) {
                System.out.println("Archivo " + USUARIOS_FILE + " no existe. Creando uno vacío...");
                guardarUsuarios(listaUsuario);
                return true;
            }

            // Leer archivo
            try (Reader reader = new FileReader(archivo, StandardCharsets.UTF_8)) {
                Type tipoLista = new TypeToken<ArrayList<usuario>>(){}.getType();
                ArrayList<usuario> usuarios = gson.fromJson(reader, tipoLista);

                if (usuarios != null) {
                    listaUsuario.fromArrayList(usuarios);
                    System.out.println("Usuarios cargados: " + usuarios.size() + " usuarios.");
                    return true;
                } else {
                    System.out.println("Archivo vacío, lista de usuarios inicializada sin datos.");
                    return true;
                }
            }

        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Guarda los usuarios desde la lista a JSON
     * @param listaUsuario Lista con los datos a guardar
     * @return true si se guardó exitosamente
     */
    public static boolean guardarUsuarios(listaUsuario listaUsuario) {
        try {
            File archivo = new File(JSON_PATH + USUARIOS_FILE);
            archivo.getParentFile().mkdirs();

            ArrayList<usuario> usuarios = listaUsuario.toArrayList();

            try (Writer writer = new FileWriter(archivo, StandardCharsets.UTF_8)) {
                gson.toJson(usuarios, writer);
                System.out.println("Usuarios guardados: " + usuarios.size() + " usuarios.");
                return true;
            }

        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ==================== MÉTODOS PARA PRODXUSU ====================

    /**
     * Carga los datos de ProdXUsu desde JSON
     * IMPORTANTE: Carga TODO el archivo pero la lista filtra por usuarioActual
     * @param listaProdXUsu Lista donde cargar los datos (debe tener usuarioActual ya establecido)
     * @return true si se cargó exitosamente
     */
    public static boolean cargarProdXUsu(listaProdXusu listaProdXUsu) {
        try {
            File archivo = new File(JSON_PATH + PRODXUSU_FILE);

            // Si el archivo no existe, crear uno vacío
            if (!archivo.exists()) {
                System.out.println("Archivo " + PRODXUSU_FILE + " no existe. Creando uno vacío...");
                guardarProdXUsu(listaProdXUsu);
                return true;
            }

            // Leer archivo completo (todos los usuarios)
            try (Reader reader = new FileReader(archivo, StandardCharsets.UTF_8)) {
                Type tipoLista = new TypeToken<ArrayList<prodXusu>>(){}.getType();
                ArrayList<prodXusu> todosProdXUsu = gson.fromJson(reader, tipoLista);

                if (todosProdXUsu != null) {
                    // fromArrayList filtra automáticamente por usuarioActual
                    listaProdXUsu.fromArrayList(todosProdXUsu);
                    System.out.println("ProdXUsu cargado y filtrado para el usuario actual.");
                    return true;
                } else {
                    System.out.println("Archivo vacío, lista ProdXUsu inicializada sin datos.");
                    return true;
                }
            }

        } catch (IOException e) {
            System.err.println("Error al cargar ProdXUsu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Guarda los datos de ProdXUsu a JSON
     * IMPORTANTE: Combina los datos del usuario actual con los de otros usuarios existentes
     * @param listaProdXUsu Lista con los datos del usuario actual
     * @return true si se guardó exitosamente
     */
    public static boolean guardarProdXUsu(listaProdXusu listaProdXUsu) {
        try {
            File archivo = new File(JSON_PATH + PRODXUSU_FILE);
            archivo.getParentFile().mkdirs();

            ArrayList<prodXusu> datosActualizados = new ArrayList<>();

            // 1. Leer datos existentes de TODOS los usuarios
            if (archivo.exists()) {
                try (Reader reader = new FileReader(archivo, StandardCharsets.UTF_8)) {
                    Type tipoLista = new TypeToken<ArrayList<prodXusu>>(){}.getType();
                    ArrayList<prodXusu> datosExistentes = gson.fromJson(reader, tipoLista);

                    if (datosExistentes != null) {
                        // 2. Filtrar: mantener solo datos de OTROS usuarios
                        String usuarioActual = listaProdXUsu.getUsuarioActual();
                        for (prodXusu prodxusu : datosExistentes) {
                            if (!prodxusu.getUsuario().equals(usuarioActual)) {
                                datosActualizados.add(prodxusu);
                            }
                        }
                    }
                }
            }

            // 3. Agregar los datos del usuario actual
            ArrayList<prodXusu> datosUsuarioActual = listaProdXUsu.toArrayList();
            datosActualizados.addAll(datosUsuarioActual);

            // 4. Guardar todo combinado
            try (Writer writer = new FileWriter(archivo, StandardCharsets.UTF_8)) {
                gson.toJson(datosActualizados, writer);
                System.out.println("ProdXUsu guardado: " + datosActualizados.size() + " registros totales.");
                return true;
            }

        } catch (IOException e) {
            System.err.println("Error al guardar ProdXUsu: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene el usuario actual de la lista (método auxiliar)
     * @param listaProdXUsu Lista de ProdXUsu
     * @return Nombre del usuario actual
     */
    private static String getUsuarioActual(listaProdXusu listaProdXUsu) {
        ArrayList<prodXusu> datos = listaProdXUsu.toArrayList();
        if (!datos.isEmpty()) {
            return datos.get(0).getUsuario();
        }
        return null;
    }

    // ==================== MÉTODOS DE UTILIDAD ====================

    /**
     * Inicializa todos los archivos JSON si no existen
     */
    public static void inicializarArchivos() {
        File carpeta = new File(JSON_PATH);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
            System.out.println("Carpeta de base de datos creada.");
        }

        // Crear archivos vacíos si no existen
        crearArchivoSiNoExiste(INVENTARIO_FILE, "[]");
        crearArchivoSiNoExiste(USUARIOS_FILE, "[]");
        crearArchivoSiNoExiste(PRODXUSU_FILE, "[]");
    }

    /**
     * Crea un archivo con contenido inicial si no existe
     */
    private static void crearArchivoSiNoExiste(String nombreArchivo, String contenidoInicial) {
        File archivo = new File(JSON_PATH + nombreArchivo);
        if (!archivo.exists()) {
            try (Writer writer = new FileWriter(archivo, StandardCharsets.UTF_8)) {
                writer.write(contenidoInicial);
                System.out.println("Archivo " + nombreArchivo + " creado.");
            } catch (IOException e) {
                System.err.println("Error al crear archivo " + nombreArchivo + ": " + e.getMessage());
            }
        }
    }

    /**
     * Guarda todos los datos del sistema
     */
    public static boolean guardarTodo(listaInventario inventario,
                                      listaUsuario usuarios,
                                      listaProdXusu prodXUsu) {
        boolean inventarioOk = guardarInventario(inventario);
        boolean usuariosOk = guardarUsuarios(usuarios);
        boolean prodXUsuOk = guardarProdXUsu(prodXUsu);

        return inventarioOk && usuariosOk && prodXUsuOk;
    }

    /**
     * Carga todos los datos del sistema
     */
    public static boolean cargarTodo(listaInventario inventario,
                                     listaUsuario usuarios,
                                     listaProdXusu prodXUsu) {
        boolean inventarioOk = cargarInventario(inventario);
        boolean usuariosOk = cargarUsuarios(usuarios);
        // ProdXUsu se carga después del login cuando se establece usuarioActual

        return inventarioOk && usuariosOk;
    }
}