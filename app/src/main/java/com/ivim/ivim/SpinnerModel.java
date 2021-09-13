package com.ivim.ivim;

public class SpinnerModel {
    private String nombre;
    private String imagen;
    public void ponerNombre(String nombre){this.nombre = nombre;}
    public void ponerImagen(String imagen) {this.imagen = imagen;}
    public String dameNombre(){return this.nombre;}
    public String dameImagen() {return this.imagen;}
}