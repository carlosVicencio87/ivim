package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ModeloRecycler extends AppCompatActivity {
    private String modelo_bascula;

    public ModeloRecycler(String model_bascula){

        this.modelo_bascula=model_bascula;
    }

    public String getModeloBascula() { return modelo_bascula; }

    public void setModelo_bascula(String nombre_bascula) {
        this.modelo_bascula = nombre_bascula; }
}