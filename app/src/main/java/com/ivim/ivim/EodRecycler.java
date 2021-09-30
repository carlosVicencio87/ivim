package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EodRecycler extends AppCompatActivity {
    private String eod_bascula;

    public EodRecycler(String eoD_bascul){

        this.eod_bascula =eoD_bascul;
    }

    public String getEod_bascula() { return eod_bascula; }

    public void setEod_bascula(String eod_bascula) {
        this.eod_bascula = eod_bascula; }
}