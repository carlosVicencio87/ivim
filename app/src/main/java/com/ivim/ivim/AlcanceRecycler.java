package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

public class AlcanceRecycler extends AppCompatActivity {
    private String alcance_bascula;

    public AlcanceRecycler(String alcanc_bascula){

        this.alcance_bascula =alcanc_bascula;
    }

    public String getAlcance_bascula() { return alcance_bascula; }

    public void setAlcance_bascula(String alcance_bascula) {
        this.alcance_bascula = alcance_bascula; }
}