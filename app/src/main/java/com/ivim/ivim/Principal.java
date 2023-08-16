package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Principal extends AppCompatActivity {

    private LinearLayout pedir_folios,mensaje_sn_folios;
    private SharedPreferences id_sher,nombre_sher;
    private SharedPreferences datosVerificador;
    private SharedPreferences.Editor editor;
    private String id_str, nombre_str,token_cambio;

    private ExecutorService executorService;
    private static String SERVIDOR_CONTROLADOR;
    private JSONArray json_folios;
    private TextView aceptar_sn_folios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_principal);
        executorService= Executors.newSingleThreadExecutor();
        SERVIDOR_CONTROLADOR = new Servidor().local;

        pedir_folios=findViewById(R.id.pedir_folios);
        mensaje_sn_folios=findViewById(R.id.mensaje_sn_folios);
        aceptar_sn_folios=findViewById(R.id.aceptar_sn_folios);

        id_sher=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        id_str= id_sher.getString("id","no hay");
        nombre_sher=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        nombre_str = nombre_sher.getString("nombres","no hay");
        Log.e("id_str",id_str);
        Log.e("nombre_str",nombre_str);

        datosVerificador = getSharedPreferences("Usuario",this.MODE_PRIVATE);
        editor=datosVerificador.edit();
        pedir_folios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        pedirFolios();


                    }
                });
            }
        });
        aceptar_sn_folios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensaje_sn_folios.setVisibility(View.GONE);
            }
        });
    }
    public void pedirFolios()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"pedir_folios.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta:",response);
                        if (response.equals("no existe")){
                            mensaje_sn_folios.setVisibility(View.VISIBLE);
                        }
                        else{
                            editor.putString("folio",response);
                            editor.apply();
                            Intent intent = new Intent(Principal.this, Mapa.class);
                            startActivity(intent);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e( "error", "error: " +error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id_str);
                return map;
            }
        };
        requestQueue.add(request);
    }
}