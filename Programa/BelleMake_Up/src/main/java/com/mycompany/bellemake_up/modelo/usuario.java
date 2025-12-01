/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bellemake_up.modelo;

/**
 *
 * @author yennf
 */
public class usuario {
    public String usuario;
    public String nombre;
    public String contra;
    public String correo;
    public String rol;

    public String getUsuario() {
        return usuario;
    }

     public usuario() {}
    
    
    public usuario(String usuario, String nombre, String contra, String correo, String rol) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.contra = contra;
        this.correo = correo;
        this.rol = rol;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    
}
