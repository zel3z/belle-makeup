/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author yennf
 */
public class prodXusu {
 public String usuario;
    public String idprod;
    public boolean favorito;
    public boolean carrito;
    public LocalDate comprado;
    public int cantiComprado;

    public String getIdprod() {
        return idprod;
    }

    public prodXusu() {}

    public prodXusu(String usu, String idprod, boolean favorito, boolean carrito, LocalDate comprado, int cantiComprado) {
        this.usuario= usu;
        this.idprod = idprod;
        this.favorito = favorito;
        this.carrito = carrito;
        this.comprado = comprado;
        this.cantiComprado = cantiComprado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setIdprod(String idprod) {
        this.idprod = idprod;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public boolean isCarrito() {
        return carrito;
    }

    public void setCarrito(boolean carrito) {
        this.carrito = carrito;
    }

    public LocalDate getComprado() {
        return comprado;
    }

    public void setComprado(LocalDate comprado) {
        this.comprado = comprado;
    }

    public int getCantiComprado() {
        return cantiComprado;
    }

    public void setCantiComprado(int cantiComprado) {
        this.cantiComprado = cantiComprado;
    }
    
    
}
