package com.mycompany.bellemake_up.modelo;

import java.util.ArrayList;

public interface JsonSerializable<T> {
    // pasar la lista a ArrayList para Gson, primer paso serialización
    ArrayList<T> toArrayList();

    // llenar la lista desde ArrayList, ultimo paso deserializarión
    void fromArrayList(ArrayList<T> datos);

    // Limpia la lista antes de cargar nuevos nodos/objetos
    void limpiar();
}
