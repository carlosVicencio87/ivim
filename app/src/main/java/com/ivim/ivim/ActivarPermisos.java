package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ActivarPermisos extends AppCompatActivity {

    private ConstraintLayout confirmar_actualizar;
    private TextView confirmar_si,confirmar_no;
    private static  int PERMISO_LOCATION=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_activar_permisos);

        confirmar_actualizar=findViewById(R.id.confirmar_actualizar);
        confirmar_si=findViewById(R.id.confirmar_si);
        confirmar_no=findViewById(R.id.confirmar_no);


        confirmar_si.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intento2= new Intent( ActivarPermisos.this,Mapa.class);
                startActivity(intento2);
                confirmar_actualizar.setVisibility(View.GONE);
            }

        });
        confirmar_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final int permisoLocacion = ContextCompat.checkSelfPermission(ActivarPermisos.this, Manifest.permission.ACCESS_FINE_LOCATION);

                if (permisoLocacion!= PackageManager.PERMISSION_GRANTED) {
                    solicitarPermisoLocation();
                }
                else{
                    confirmar_actualizar.setVisibility(View.VISIBLE);
                }

            }

        });
    }
    private void solicitarPermisoLocation() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISO_LOCATION);
    }


}