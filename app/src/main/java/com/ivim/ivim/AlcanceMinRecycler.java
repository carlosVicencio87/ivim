package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

public class AlcanceMinRecycler extends AppCompatActivity {
    private String min_bascula;

    public AlcanceMinRecycler(String min_bascul){

        this.min_bascula =min_bascul;
    }

    public String getMin_bascula() { return min_bascula; }

    public void setMin_bascula(String min_bascula) {
        this.min_bascula = min_bascula; }
}