package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private AsincronaCatalogo asincronaCatalogo;
    private AsincronaBascula asincronaBascula;
    private Asincrona asincrona;
    private EditText correo,contrasena;
    private TextView ingresar,recuperarContra,mensaje;
    private String valCorreo,valContra,correo_final;
    private static String SERVIDOR_CONTROLADOR;
    private int check=0;
    private SharedPreferences datosUsuario;
    private SharedPreferences.Editor editor;
    private boolean correo_exitoso,contrasena_exitoso;
    private  JSONArray json_datos_usuario;
    private  JSONArray json_datos_tabla;
    private  JSONArray json_datos_basculas;
    private  String strInicio,strUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        SERVIDOR_CONTROLADOR = new Servidor().getLocalHost();
        datosUsuario = getSharedPreferences("Usuario",this.MODE_PRIVATE);
        editor=datosUsuario.edit();


        correo=findViewById(R.id.correo);
        contrasena =findViewById(R.id.contrasena);
        ingresar= findViewById(R.id.ingresar);
        recuperarContra =findViewById(R.id.recuperarContra);
        mensaje =findViewById(R.id.mensaje);

        checkSesion();

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valCorreo=correo.getText().toString();
                valContra=contrasena.getText().toString();
                Log.e("datocorreo",valCorreo );
                Log.e("datocontra",valContra );
                if(!valCorreo.trim().equals("")){
                    if(!valContra.trim().equals("")){
                        if(correo_exitoso==true){

                                
                                recuperarContra.setVisibility(View.GONE);
                                ingresar.setVisibility(View.GONE);
                                mensaje.setText("Iniciando sesión ...");
                                mensaje.setVisibility(View.VISIBLE);
                                asincrona= new Asincrona();
                                asincrona.execute();
                                asincronaCatalogo= new Login.AsincronaCatalogo();
                                asincronaCatalogo.execute();
                                asincronaBascula= new AsincronaBascula();
                                asincronaBascula.execute();

                        }
                        else{
                            correo_exitoso = false;
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "La contrasena es necesario.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "El correo es necesario.", Toast.LENGTH_LONG).show();
                }

            }

        });
        recuperarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intento2= new Intent( Login.this,Recuperar_Contra.class);
                //startActivity(intento2);
            }
        });

        correo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {

                if(!tieneFoco)
                {
                    correo_final=correo.getText().toString().trim().toLowerCase();
                    if (!correo_final.equals("")&&correo_final!=null)
                    {
                        // String regex = "^(.+)@(.+)$";

                        String regexUsuario = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                        Pattern pattern = Pattern.compile(regexUsuario);
                        Matcher matcher = pattern.matcher(correo_final);
                        if(matcher.matches()==true){

                            correo_exitoso=true;

                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Ingrese correo valido.",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private class Asincrona extends AsyncTask<Void, Integer,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            hacerPeticion();
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
    public void hacerPeticion()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"inicio_sesion_usuarios.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta:",response);
                        if (response.equals("no_existe")) {
                            recuperarContra.setVisibility(View.VISIBLE);
                            ingresar.setVisibility(View.VISIBLE);
                            mensaje.setText("El teléfono o correo es incorrecto.");
                        }
                        else
                        {
                            try {

                                  json_datos_usuario=new JSONArray(response);
                                    Log.e("lala",""+json_datos_usuario);
                                for (int i=0;i<json_datos_usuario.length();i++){
                                    JSONObject jsonObject = json_datos_usuario.getJSONObject(i);
                                    //Log.e("nombreMovies", String.valueOf(jsonObject));
                                    String strId = jsonObject.getString("id");
                                    String strNombre = jsonObject.getString("nombres");
                                    String strApellido_1 = jsonObject.getString("apellido_1");
                                    String strApellido_2=jsonObject.getString("apellido_2");
                                    String strTelefono=jsonObject.getString("telefono");
                                    String strCorreo=jsonObject.getString("correo");
                                    String strContrasena=jsonObject.getString("contra");
                                    String strActivo = jsonObject.getString("activo");
                                    String strId_sesion=jsonObject.getString("id_sesion");
                                    String strFecha_registro = jsonObject.getString("fecha_de_ingreso");

                                    String strFecha_de_ingreso=jsonObject.getString("fecha_de_ingreso");
                                    String strAltitud=jsonObject.getString("altitud");
                                    String strLongitud=jsonObject.getString("longitud");
                                    String strUltima_fecha_de_conexion=jsonObject.getString("ultima_fecha_de_conexion");
                                    String strUltima_alt=jsonObject.getString("ultima_alt");
                                    String strUltima_long=jsonObject.getString("ultima_long");
                                    String strZona=jsonObject.getString("zona");

                                    editor.putString("id",strId);
                                    editor.putString("nombres",strNombre);
                                    editor.putString("apellido_1",strApellido_1);
                                    editor.putString("apellido_2",strApellido_2);
                                    editor.putString("telefono",strTelefono);
                                    editor.putString("correo",strCorreo);
                                    editor.putString("contrasena",strContrasena);
                                    editor.putString("activo",strActivo);
                                    editor.putString("fecha_registro",strFecha_registro);
                                    editor.putString("id_sesion",strId_sesion);
                                    editor.putString("fecha_de_ingreso",strFecha_de_ingreso);
                                    editor.putString("altitud",strAltitud);
                                    editor.putString("longitud",strLongitud);
                                    editor.putString("ultima_fecha_de_conexion",strUltima_fecha_de_conexion);
                                    editor.putString("ultima_alt",strUltima_alt);
                                    editor.putString("ultima_long",strUltima_long);
                                    editor.putString("zona",strZona);
                                    editor.apply();
                                    Log.e("1",""+strNombre);
                                    Log.e("2",strApellido_1);
                                    Log.e("3",strApellido_2);
                                    Log.e("4",strTelefono);
                                    Log.e("4",strCorreo);
                                    Log.e("5",strContrasena);
                                    Log.e("6",strActivo);
                                    Log.e("7",""+strFecha_registro);
                                    Log.e("8",strId_sesion);
                                    Log.e("9",strFecha_de_ingreso);
                                    Log.e("10",strAltitud);
                                    Log.e("11",strLongitud);
                                    Log.e("12",strUltima_fecha_de_conexion);
                                    Log.e("13",strUltima_alt);
                                    Log.e("14",""+strUltima_long);
                                    Log.e("15",strZona);

                                    Intent intent = new Intent(Login.this, Mapa.class);
                                    startActivity(intent);
                                }
                            }
                            catch (JSONException e) {
                                Log.e("errorRespuesta", String.valueOf(e));
                            }
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
                map.put("correo",valCorreo);
                map.put("contrasena",valContra);
                return map;
            }
        };
        requestQueue.add(request);
    }

    private class AsincronaCatalogo extends AsyncTask<Void, Integer,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            pedir_tabla();
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
    public void pedir_tabla()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"pedir_tabla.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta2:",response + "");
                        try{
                            json_datos_tabla=new JSONArray(response);
                            Log.e("tabla",""+json_datos_tabla);
                            for(int i=0; i<json_datos_tabla.length();i++){
                                
                            }

                        }

                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("respuesta1", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();

                return map;
            }
        };
        requestQueue.add(request);
    }
    private class AsincronaBascula extends AsyncTask<Void, Integer,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            pedir_bascula();
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
    public void pedir_bascula()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"pedir_bascula.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta23:",response + "");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("respuesta11", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();

                return map;
            }
        };
        requestQueue.add(request);
    }
    private void checkSesion() {
        strInicio = datosUsuario.getString("id_sesion", "no");

        Log.e("inicio",""+strInicio);
        if (!strInicio.equals("no"))
        {

            Log.e("idsesion_main",strInicio);
            Intent agenda= new Intent(Login.this, Mapa.class);
            startActivity(agenda);
        }
    }
}