/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author yennf
 */
public class producto {
    public String idprod;
    public String nomprod;
    public float precio;
    public String descri;
    public String categoria;
    public int stock;
    public int vendidos;
    public String rutaImagen;

    public String getRutaImagen() {
        return rutaImagen;
    }
    
    public producto() {}

    public producto(String idprod, String nomprod, float precio, String descri,
            String categoria, int stock, int vendidos, String rutaImagen) {
        this.idprod = idprod;
        this.nomprod = nomprod;
        this.precio = precio;
        this.descri = descri;
        this.categoria = categoria;
        this.stock = stock;
        this.vendidos = vendidos;
        this.rutaImagen = rutaImagen;
    }
    
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
    
    public String getIdprod() {
        return idprod;
    }

    public void setIdprod(String idprod) {
        this.idprod = idprod;
    }

    public String getNomprod() {
        return nomprod;
    }

    public void setNomprod(String nomprod) {
        this.nomprod = nomprod;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getDesc() {
        return descri;
    }

    public void setDesc(String desc) {
        this.descri = desc;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getVendidos() {
        return vendidos;
    }

    public void setVendidos(int vendidos) {
        this.vendidos = vendidos;
    }
    
    
}
