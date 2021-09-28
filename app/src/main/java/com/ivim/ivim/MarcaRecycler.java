package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MarcaRecycler extends AppCompatActivity {
    private String marca_bascula;

    public MarcaRecycler(String mark_bascula){

        this.marca_bascula=mark_bascula;
    }

    public String getMarca_basculaBascula() { return marca_bascula; }

    public void setMarca_bascula(String nombre_bascula) {
        this.marca_bascula = nombre_bascula; }
}