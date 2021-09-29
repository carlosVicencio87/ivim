package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

public class ModeloRecycler extends AppCompatActivity {
    private String modelo_bascula;

    public ModeloRecycler(String model_bascula){

        this.modelo_bascula =model_bascula;
    }

    public String getModelo_bascula() { return modelo_bascula; }

    public void setModelo_bascula(String modelo_bascula) {
        this.modelo_bascula = modelo_bascula; }
}