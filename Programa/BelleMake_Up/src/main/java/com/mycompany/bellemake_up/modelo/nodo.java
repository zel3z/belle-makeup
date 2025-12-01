/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bellemake_up.modelo;

/**
 *
 * @author yennf
 */
public class nodo<N> {
    public N info;
    public nodo<N> sig;
    public nodo<N> ant;
    
    public nodo(N dato){
        this.info=dato;
        sig=ant=null;
    }
}
