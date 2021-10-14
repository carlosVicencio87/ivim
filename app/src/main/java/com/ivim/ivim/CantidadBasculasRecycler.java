package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

public class CantidadBasculasRecycler extends AppCompatActivity {
    private String cantidad_bascula;

    public CantidadBasculasRecycler(String cant_bascula){

        this.cantidad_bascula =cant_bascula;
    }

    public String getCantidad_bascula() { return cantidad_bascula; }

    public void setCantidad_bascula(String cantidad_bascula) {
        this.cantidad_bascula = cantidad_bascula; }
}