package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

public class ModeloRecycler extends AppCompatActivity {
    private String modelo_bascula;

    public ModeloRecycler(String mark_bascula){

        this.modelo_bascula =mark_bascula;
    }

    public String getMarca_basculaBascula() { return modelo_bascula; }

    public void setModelo_bascula(String modelo_bascula) {
        this.modelo_bascula = modelo_bascula; }
}