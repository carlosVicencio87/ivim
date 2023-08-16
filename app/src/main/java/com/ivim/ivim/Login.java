package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private ExecutorService executorService;


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
    private Conexion conexion1,conexion2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        executorService= Executors.newSingleThreadExecutor();
        SERVIDOR_CONTROLADOR = new Servidor().local;
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
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    hacerPeticion();
                                    pedir_tabla();
                                    pedir_bascula();

                                }
                            });


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


    public void hacerPeticion()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"inicioSesion.php",
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
                                    String strContrasena=jsonObject.getString("password");
                                    String strActivo = jsonObject.getString("estatus");
                                    String strId_sesion=jsonObject.getString("id_sesion");
                                    String strFecha_registro = jsonObject.getString("fecha_ingreso");


                                    String strAltitud=jsonObject.getString("latitud");
                                    String strLongitud=jsonObject.getString("longitud");
                                    String strUltima_fecha_de_conexion=jsonObject.getString("ultima_fecha_conexion");

                                    String strZona=jsonObject.getString("zona");
                                    Log.e("idsesion",strId_sesion);


                                    editor.putString("id",strId);
                                    editor.putString("nombres",strNombre);
                                    editor.putString("apellido_1",strApellido_1);
                                    editor.putString("apellido_2",strApellido_2);
                                    editor.putString("telefono",strTelefono);
                                    editor.putString("correo",strCorreo);
                                    editor.putString("password",strContrasena);
                                    editor.putString("estatus",strActivo);
                                    editor.putString("fecha_registro",strFecha_registro);
                                    editor.putString("id_sesion",strId_sesion);
                                    editor.putString("fecha_de_ingreso",strFecha_registro);
                                    editor.putString("latitud",strAltitud);
                                    editor.putString("longitud",strLongitud);
                                    editor.putString("ultima_fecha_de_conexion",strUltima_fecha_de_conexion);

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
                                    Log.e("idsesion",strId_sesion);
                                    Log.e("9",strFecha_registro);
                                    Log.e("10",strAltitud);
                                    Log.e("11",strLongitud);
                                    Log.e("12",strUltima_fecha_de_conexion);


                                    Log.e("15",strZona);

                                    Intent intent = new Intent(Login.this, Principal.class);
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
                map.put("contra",valContra);
                return map;
            }
        };
        requestQueue.add(request);
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
                            conexion1=new Conexion(getApplicationContext(),"catalogo",null,1);
                            SQLiteDatabase database1 = conexion1.getWritableDatabase();
                            database1.execSQL(Sentencias.DROP_CATALOGO);
                            database1.execSQL(Sentencias.TABLA_CATALOGO);
                            String TABLE_NAME = "catalogo";
                            Cursor cursor = database1.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_NAME + "'", null);
                            Log.e("dbExiste:",cursor.getCount()+" -- ");
                            if (cursor.getCount()==0){
                                database1.execSQL(Sentencias.TABLA_CATALOGO);
                            }
                            for(int i=0; i<json_datos_tabla.length();i++){
                                JSONObject jsonObject = json_datos_tabla.getJSONObject(i);
                                String strId = jsonObject.getString("id");
                                String  strAlcanceMaximo = jsonObject.getString("alcance_maximo");
                                String  strEod = jsonObject.getString("eod");
                                String  strTipo = jsonObject.getString("tipo");
                                String  strAlcanceMinimo = jsonObject.getString("alcance_minimo");
                                String  strAlcanceMedicion = jsonObject.getString("alcance_medicion");
                                String  strClaseExactitud= jsonObject.getString("clase_exactitud");
                                String  strMarcoPesas= jsonObject.getString("marco_pesas");
                                String  strPesa5kg= jsonObject.getString("pesa_5kg");
                                String  strPesa10kg= jsonObject.getString("pesa_10kg");
                                String  strPesa20kg= jsonObject.getString("pesa_20kg");
                                String  strPesaClaseExactitud= jsonObject.getString("pesa_clase_exactitud");
                                String  strHorario= jsonObject.getString("horario");


                                ContentValues valores = new ContentValues();
                                String str = "";
                                valores.put("alcance_maximo",strAlcanceMaximo);
                                valores.put("eod",strEod);
                                valores.put("tipo",strTipo);
                                valores.put("alcance_minimo",strAlcanceMinimo);
                                valores.put("alcance_medicion",strAlcanceMedicion);
                                valores.put("clase_exactitud",strClaseExactitud);
                                valores.put("marco_pesas",strMarcoPesas);
                                valores.put("pesa_5kg",strPesa5kg);
                                valores.put("pesa_10kg",strPesa10kg);
                                valores.put("pesa_20kg",strPesa20kg);
                                valores.put("pesa_clase_exactitud",strPesaClaseExactitud);
                                valores.put("horario",strHorario);
                                Long id_res= database1.insert("catalogo","id",valores);

                            }

                        }

                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.    e("respuesta1", error.getMessage());
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

    public void pedir_bascula()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"pedir_bascula.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta23:",response + "");
                        try{
                            json_datos_basculas=new JSONArray(response);
                            Log.e("tabla",""+json_datos_tabla);
                            conexion2=new Conexion(getApplicationContext(),"basculas",null,1);
                            SQLiteDatabase database2 = conexion2.getWritableDatabase();
                            database2.execSQL(Sentencias.DROP_BASCULAS);
                            database2.execSQL(Sentencias.TABLA_BASCULAS);


                            String TABLE_NAME2 = "basculas";

                            Cursor cursor2 = database2.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_NAME2 + "'", null);

                            Log.e("dbExiste:",cursor2.getCount()+" -- ");


                            if (cursor2.getCount()==0){
                                database2.execSQL(Sentencias.TABLA_BASCULAS);
                            }
                            for(int i=0; i<json_datos_basculas.length();i++){
                                JSONObject jsonObject = json_datos_basculas.getJSONObject(i);
                                String strId = jsonObject.getString("id");
                                String  strMarca = jsonObject.getString("marca");
                                String  strModelo = jsonObject.getString("modelo");
                                String  strNumeroAprobacion = jsonObject.getString("numero_aprobacion");
                                String  strCodigoMarca = jsonObject.getString("codigo_marca");
                                String  strCodigoModelo= jsonObject.getString("codigo_modelo");
                                String  strAnoAprobacion= jsonObject.getString("ano_aprobacion");
                                String  strAlcanceMinimo=jsonObject.getString("alcance_minimo");
                                String  strAlcanceMaximo= jsonObject.getString("alcance_maximo");

                                if(strAlcanceMinimo.equals("")){
                                    strAlcanceMinimo=" ";
                                }
                                if(strAlcanceMaximo.equals("")){
                                    strAlcanceMaximo=" ";
                                }
                                String string = "";
                                ContentValues valores2 = new ContentValues();
                                valores2.put("marca",strMarca);
                                valores2.put("modelo",strModelo);
                                valores2.put("numero_aprobacion",strNumeroAprobacion);
                                valores2.put("codigo_marca",strCodigoMarca);
                                valores2.put("codigo_modelo",strCodigoModelo);
                                valores2.put("ano_aprobacion",strAnoAprobacion);
                                valores2.put("alcance_minimo",strAlcanceMinimo);

                                valores2.put("alcance_maximo",strAlcanceMaximo);
                                Long id_res2= database2.insert("basculas","id",valores2);

                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
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
            Intent agenda= new Intent(Login.this, Principal.class);
            startActivity(agenda);
        }
    }
}