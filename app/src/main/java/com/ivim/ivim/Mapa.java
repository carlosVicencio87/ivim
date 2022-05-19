package com.ivim.ivim;

import static com.ivim.ivim.CoordinateConverter.fromGeodeticToUTM;
import static com.ivim.ivim.CoordinateConverter.fromUTMToGeodetic;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int PERMISO_LOCATION = 1;

    private LocationManager locationManager;
    private LatLng coord, coordenadas, latLong;
    private Marker marker;
    private ConstraintLayout mapaid,cons_check,caja_siguiente_basc,caja_mensaje_basc,caja_finalizar_basc,caja_verificar_numAprobacion,
            cons_factura,caja_integridad,cons_relacion_comercial,cons_conflicto_intereses;
    private LinearLayout caja_punto_partida, caja_edit_nombre, caja_nombre_final,
            caja_direccion, caja_giro, caja_mercado, caja_mercado_final,caja_edit_tel, caja_tel_final,
            caja_recycler_marca, caja_recycler_modelo, caja_siguiente_tab, caja_x, caja_longitud,
            caja_zona,caja_instrumento, caja_alcanceSnaprobacion, caja_alcanceSnaprobacion_final, caja_recycler_alcance,caja_snAprobacion_marca,
            caja_recycler_eod,caja_recycler_minimo,caja_exactitud,caja_edit_costo,
            caja_auto_aproba,caja_aprobacion_final,caja_recycler_basculas,caja_codigo_marca,caja_codigo_modelo,caja_ano_aprobacion,
            caja_costo_final,caja_edit_fecha,caja_fecha_final,caja_marco_pesas,caja_pesa5k,caja_pesa10k,caja_pesa20k,caja_pesa_clase_exactitud,
            caja_horario,caja_snAprobacion_marca_final,caja_alcanceMinSnaprobacion,caja_alcanceSMinnaprobacion_final,caja_snAprobacion_modelo,
            caja_snAprobacion_modelo_final,caja_Codigomarca_Snaprobacion_final,caja_Codigomarca_Snaprobacion,
            caja_CodigomaModelo_Snaprobacion,caja_CodigomaModelo_Snaprobacion_final
            ,caja_ano_Snaprobacion,caja_ano_Snaprobacion_final,caja_tipo_visita,
            caja_edit_rfc,caja_rfc_final,caja_edit_numSerie,caja_numSerie_final,caja_orden_tipoVerificacion,caja_orden_merca,caja_orden_modelo,caja_orden_numSerie,
            caja_orden_alcanceMax,caja_orden_eod,caja_orden_alcanceMin,caja_orden_modeloPrototipo,caja_orden_claseExactitud,caja_orden_alcanceMedicion;

    private Fragment map;
    private int check = 0;
    private int tiempo_actualizacion = 20000;
    private SharedPreferences sharedPreferences,modeloSHER,alcanceMaxSher,eodSher,
            alcanceMinSher,idSher,id_SesionSher;
    private SharedPreferences.Editor editormodelo,editorAlcanceMax,editorEod,
            editorAlcanceMin,editor,z;

    private Mapa activity;
    private double latitud, longitud, altitud, latUpdate, longUpdate;
    private String direccion, nuevo_nombre, seleccion_giro, nuevo_tel, nueva_serie,
            nuevo_costo,seleccion_instrumento,selector_modelo,checkModel,checkAlcanceMax,checkEod,checkAlcanceMin,
            seleccion_exactitud,nueva_aprobacion,valorCheckboxPrimera,fecha_final_Str,tipo_intrumento,valcatalogo,nueva_marca,
            nueva_modelo,nueva_alcanceMax,nueva_alcanceMin,nueva_codigoMarca,nueva_codigoModelo,nueva_anoSnapro,nueva_fecha,
            valorCheckboxFactura,ano_global,nuevo_coodigoMarca,nuevo_coodigoModelo,nuevo_eod,nuevo_alcanceMax_aprobado,
            nuevo_alcanceMin_aprobado,nuevo_tipoInstrumento,nueva_claseExactitud,nuevo_marco_pesas,nuevo_pesas_5kg,nuevo_pesas_10kg,nuevo_pesas_20kg
            ,nuevo_horario,nuevo_pesa_clase_exactitud,nuevo_alcanceMax_aprobado_str,nuevo_alcanceMin_aprobado_str,
            nuevo_tipoInstrumento_string,nueva_claseExactitud_string,nuevo_marco_pesas_string,nuevo_pesas_5kg_string,nuevo_pesas_10kg_string
            ,nuevo_pesas_20kg_string,nuevo_pesa_clase_exactitud_string,nuevo_horario_string,nuevo_coodigoMarca_string,
            nuevo_coodigoModelo_string,ano_global_string,nuevo_eod_str,eodSnaprobacion,tipoInstrumentoSnaprobacion,
            claseExactitudSnaprobacion,marco_pesasSnaprobacion,pesas_5kgSnaprobacion,pesas_10kgSnaprobacion,pesas_20kgSnaprobacion,
            esa_clase_exactitudSnaprobacion,horarioSnaprobacion,nueva_codigoMarcaSnaprobacion,
            nueva_codigoModelo_snAprobacion,tipo_visita_string,enviar_modelo,enviar_AlcanceMax,
            enviar_numero_aprobacion,enviar_marca,enviar_AlcanceMin,enviar_CodigoMarca,
            enviar_CodigoModelo,enviar_anoAprobacion,enviar_eod,enviar_TipoInstrumento,enviar_claseExactitud,
            enviar_marcoPesas,enviar_pesas5kg,enviar_pesas10kg,enviar_pesas20kg,enviar_pesaClase_exactitud,
            enviar_horario,enviar_TipoVisita,enviar_costo,nuevo_mercado,nueva_fecha_final,nuevo_rfc,nuevo_numSerie,enviar_numSerie,str_final,
            calle_str,colonia_str,delegacion_str,cp_str,ciudad_str,pais_str,nueva_zona,nueva_x,nueva_y,id_usuer,id_SesionUsuer,
            valorCheckboxComercial,valorCheckboxIntegridad,valorCheckboxIntereses,nuevo_alcanceMedicion;


    private TextView puntoPartida,fecha_final,nombre,direccion_mercado,telefono,latitud_x,longitud_y,zona,alcanceSnAprobacion,costo,
            regresar_map,siguiente_tab,regresar_formulario,agregar_bascula,finalizar_reg_bascula,finalizar_no,finalizar_si,tip_model
            ,aprobacion_basc,listas_intrusmento,listas_exactitud,listas_mercado,listas_giro,regresar_otravez_formulario,agregar_otra_bascula,
            recycler_alcance,recycler_marca,recycler_minimo,recycler_eod,tipoInstrumento,claseExactitud,marco_pesas,pesas_5kg
            ,pesas_10kg,pesas_20kg,codigo_marca,codigo_modelo,ano_aprobacion,pesa_clase_exactitud,horario,alcanceMinSnAprobacion,
            aprobacion_no,aprobacion_si,marca_snAprobacion_final,modelo_snAprobacion_final,CodigomarcaSnaprobacion,
            CodigomaModeloSnaprobacion,anoSnaprobacion,tipo_visita,mercado_vista,rfc,numSerie,ir_preguntas,orden_tipoVerificacion,
            orden_merca,orden_modelo,orden_numSerie,orden_alcanceMax,orden_eod,orden_alcanceMin,orden_modeloPrototipo,orden_claseExactitud,orden_alcanceMedicion;




    private int tipo_camino,cuenta_basculas;
    private EditText nombre_texto, fecha,mercado_texto, tel_texto, marca_snAprobacion,alcanceSnAprobacion_texto,costo_texto,alcanceMinSnAprobacion_texto,
            modelo_snAprobacion,Codigomarca_Snaprobacion_texto,CodigomaModelo_Snaprobacion_texto,
            ano_Snaprobacion_texto,rfc_texto,numSerie_texto;
    private ImageView iniciar_verificacion, guardar_nombre, cambiar_nombre, guardar_tel,
            cambiar_telefono,guardar_mercado, cambiar_mercado,guardar_alcance_snAprobacion, cambiar_alcance_snAprobacion,
            guardar_costo,cambiar_costo,guardar_aprobacion,cambiar_aprobacion,guardar_fecha,cambiar_fecha,
            guardar_alcanceMin_snAprobacion,cambiar_alcanceMin_snAprobacion,guardar_marca,cambiar_marca,
            guardar_modelo,cambiar_modelo,cambiar_Codigomarca_Snaprobacion,guardar_Codigomarca_Snaprobacion,
            guardar_CodigomaModelo_Snaprobacion,cambiar_CodigomaModelo_Snaprobacion,guardar_ano_Snaprobacion
            ,cambiar_ano_Snaprobacion,guardar_rfc,cambiar_rfc,guardar_numSerie,cambiar_serie;
    private RecyclerView  recycler_modelo,recycler_numero_basc;
    private Boolean tel10,fecha_existoso,rfc_existoso;
    private ScrollView formulario_principal, formulario_bascula,almacen_basculas,ultimas_preguntas,acta_dictamen_final;
    private Spinner giros;
    private AdapterGiro adapterGiro;
    public ArrayList<SpinnerModel> listaGiro = new ArrayList<>();


    private RecyclerView recyclerMarca,recyclerModelo,recyclerAlcance,recyclerEod,
            recyclerMinimo,recyclerCantidad;


    private AdapterMarcaBasculas adapterMarcaBasculas;
    private AdapterModeloBasculas adapterModeloBasculas;


    private AdapterCantidadBasculas adapterCantidadBasculas;

    private ArrayList<ModeloRecycler> listaModelo;
    private ArrayList<MarcaRecycler> listaMarca;


    private ArrayList<CantidadBasculasRecycler>listaCantidadBasc;
    private Context context;
    public final static int WGS84 = 0;
    public final static int HAYFORD = 1;
    private final static double[] ELLIPSOID_A = {6378137.000, 6378388.000};
    private final static double[] ELLIPSOID_B = {6356752.3142449996, 6356911.946130};
    public UTMPoint p;
    public CoordinateConverter convertidor;
    public GeodeticPoint punto = new GeodeticPoint(latitud, longitud, altitud);

    private MultiAutoCompleteTextView autoAprobacion;


    private  JSONArray json_datos_bascula;
    private static String SERVIDOR_CONTROLADOR;
    private   SQLiteDatabase database,database2,database3,database4,database6;
    private Conexion conexion1,conexion2,conexion3,conexion4,conexion6;
    private Cursor cursor1,cursor2,cursor3,cursor4,cursor5,cursor6;
    private ArrayList<String> APROBACION;
    private CheckBox primera_si,primera_no,factura_si,factura_no,amenaza_integridad_si,amenaza_integridad_no,
            relacion_comercial_si,relacion_comercial_no,conflicto_intereses_si,conflicto_intereses_no;
    private  int numero_anoglobal,resta_anos,elementoNumeroMes,elementoNumerAno;
    private AsincronaEnviarBasculas asincronaEnviarBasculas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapaid = findViewById(R.id.mapaid);
        puntoPartida = findViewById(R.id.puntoPartida);
        iniciar_verificacion = findViewById(R.id.iniciar_verificacion);
        nombre = findViewById(R.id.nombre);
        nombre_texto = findViewById(R.id.nombre_texto);
        formulario_principal = findViewById(R.id.formulario_principal);
        caja_punto_partida = findViewById(R.id.caja_punto_partida);
        caja_edit_nombre = findViewById(R.id.caja_edit_nombre);
        caja_nombre_final = findViewById(R.id.caja_nombre_final);
        guardar_nombre = findViewById(R.id.guardar_nombre);
        cambiar_nombre = findViewById(R.id.cambiar_nombre);
        caja_edit_fecha = findViewById(R.id.caja_edit_fecha);
        guardar_fecha = findViewById(R.id.guardar_fecha);
        fecha = findViewById(R.id.fecha);
        caja_fecha_final = findViewById(R.id.caja_fecha_final);
        fecha_final = findViewById(R.id.fecha_final);
        cambiar_fecha = findViewById(R.id.cambiar_fecha);

        caja_edit_rfc = findViewById(R.id.caja_edit_rfc);
        rfc_texto = findViewById(R.id.rfc_texto);
        guardar_rfc = findViewById(R.id.guardar_rfc);
        caja_rfc_final = findViewById(R.id.caja_rfc_final);
        rfc = findViewById(R.id.rfc);
        cambiar_rfc = findViewById(R.id.cambiar_rfc);



        caja_direccion = findViewById(R.id.caja_direccion);
        direccion_mercado = findViewById(R.id.direccion_mercado);
        caja_giro = findViewById(R.id.caja_giro);
        giros = findViewById(R.id.giros);
        caja_mercado = findViewById(R.id.caja_mercado);
        mercado_texto = findViewById(R.id.mercado_texto);
        guardar_mercado = findViewById(R.id.guardar_mercado);
        caja_mercado_final = findViewById(R.id.caja_mercado_final);
        mercado_vista = findViewById(R.id.mercado_vista);
        cambiar_mercado = findViewById(R.id.cambiar_mercado);

        caja_edit_tel = findViewById(R.id.caja_edit_tel);
        tel_texto = findViewById(R.id.tel_texto);
        guardar_tel = findViewById(R.id.guardar_tel);
        caja_tel_final = findViewById(R.id.caja_tel_final);
        cambiar_telefono = findViewById(R.id.cambiar_telefono);
        telefono = findViewById(R.id.telefono);

        activity = this;
        setListaGiro();



        listaCantidadBasc = new ArrayList<>();
        setListaCantidadBasc();
        listaModelo = new ArrayList<>();
        APROBACION =new ArrayList<>();


        caja_siguiente_tab = findViewById(R.id.caja_siguiente_tab);
        regresar_map = findViewById(R.id.regresar_map);
        siguiente_tab = findViewById(R.id.siguiente_tab);
        formulario_bascula = findViewById(R.id.formulario_bascula);
        context = this;

        recyclerModelo = findViewById(R.id.recycler_modelo);
        recyclerModelo.setLayoutManager(new LinearLayoutManager(context));
        recyclerModelo.setItemViewCacheSize(50);

        recyclerCantidad=findViewById(R.id.recycler_numero_basc);
        recyclerCantidad.setLayoutManager(new LinearLayoutManager(context));


        caja_x = findViewById(R.id.caja_x);
        caja_longitud = findViewById(R.id.caja_longitud);
        latitud_x = findViewById(R.id.latitud_x);
        longitud_y = findViewById(R.id.longitud_y);
        caja_zona = findViewById(R.id.caja_zona);
        zona = findViewById(R.id.zona);
        caja_instrumento = findViewById(R.id.caja_instrumento);
        tipoInstrumento = findViewById(R.id.tipoInstrumento);
        caja_recycler_marca = findViewById(R.id.caja_recycler_marca);
        caja_recycler_modelo = findViewById(R.id.caja_recycler_modelo);
        recycler_marca = findViewById(R.id.recycler_marca);
        recycler_modelo = findViewById(R.id.recycler_modelo);
        caja_alcanceSnaprobacion = findViewById(R.id.caja_alcanceSnaprobacion);
        alcanceSnAprobacion_texto = findViewById(R.id.alcanceSnAprobacion_texto);
        guardar_alcance_snAprobacion = findViewById(R.id.guardar_alcance_snAprobacion);
        caja_alcanceSnaprobacion_final = findViewById(R.id.caja_alcanceSnaprobacion_final);
        alcanceSnAprobacion = findViewById(R.id.alcanceSnAprobacion);
        cambiar_alcance_snAprobacion = findViewById(R.id.cambiar_alcance_snAprobacion);
        caja_recycler_alcance = findViewById(R.id.caja_recycler_alcance);
        recycler_alcance = findViewById(R.id.recycler_alcance);
        caja_recycler_eod = findViewById(R.id.caja_recycler_eod);
        recycler_eod = findViewById(R.id.recycler_eod);
        caja_recycler_minimo = findViewById(R.id.caja_recycler_minimo);
        recycler_minimo = findViewById(R.id.recycler_minimo);
        caja_exactitud = findViewById(R.id.caja_exactitud);
        claseExactitud = findViewById(R.id.claseExactitud);
        caja_edit_costo = findViewById(R.id.caja_edit_costo);
        costo_texto = findViewById(R.id.costo_texto);
        guardar_costo = findViewById(R.id.guardar_costo);
        caja_costo_final = findViewById(R.id.caja_costo_final);
        costo = findViewById(R.id.costo);
        cambiar_costo = findViewById(R.id.cambiar_costo);

        caja_edit_numSerie = findViewById(R.id.caja_edit_numSerie);
        numSerie_texto = findViewById(R.id.numSerie_texto);
        guardar_numSerie = findViewById(R.id.guardar_numSerie);
        caja_numSerie_final = findViewById(R.id.caja_numSerie_final);
        numSerie = findViewById(R.id.numSerie);
        cambiar_serie = findViewById(R.id.cambiar_serie);





        caja_siguiente_basc = findViewById(R.id.caja_siguiente_basc);
        regresar_formulario = findViewById(R.id.regresar_formulario);
        agregar_bascula = findViewById(R.id.agregar_bascula);
        finalizar_reg_bascula = findViewById(R.id.finalizar_reg_bascula);
        autoAprobacion = findViewById(R.id.autoAprobacion);
        finalizar_no = findViewById(R.id.finalizar_no);
        finalizar_si = findViewById(R.id.finalizar_si);
        caja_mensaje_basc = findViewById(R.id.caja_mensaje_basc);
        caja_finalizar_basc = findViewById(R.id.caja_finalizar_basc);
        caja_auto_aproba = findViewById(R.id.caja_auto_aproba);
        guardar_aprobacion = findViewById(R.id.guardar_aprobacion);
        caja_aprobacion_final = findViewById(R.id.caja_aprobacion_final);
        aprobacion_basc = findViewById(R.id.aprobacion_basc);
        cambiar_aprobacion = findViewById(R.id.cambiar_aprobacion);
        caja_marco_pesas = findViewById(R.id.caja_marco_pesas);
        marco_pesas = findViewById(R.id.marco_pesas);
        caja_pesa5k = findViewById(R.id.caja_pesa5k);
        pesas_5kg = findViewById(R.id.pesas_5kg);
        caja_pesa10k = findViewById(R.id.caja_pesa10k);
        pesas_10kg = findViewById(R.id.pesas_10kg);
        caja_pesa20k = findViewById(R.id.caja_pesa20k);
        pesas_20kg = findViewById(R.id.pesas_20kg);
        recycler_marca = findViewById(R.id.recycler_marca);
        caja_codigo_marca = findViewById(R.id.caja_codigo_marca);
        codigo_marca = findViewById(R.id.codigo_marca);
        caja_codigo_modelo = findViewById(R.id.caja_codigo_modelo);
        codigo_modelo = findViewById(R.id.codigo_modelo);
        caja_ano_aprobacion = findViewById(R.id.caja_ano_aprobacion);
        ano_aprobacion = findViewById(R.id.ano_aprobacion);
        caja_pesa_clase_exactitud = findViewById(R.id.caja_pesa_clase_exactitud);
        pesa_clase_exactitud = findViewById(R.id.pesa_clase_exactitud);
        caja_horario = findViewById(R.id.caja_horario);
        horario = findViewById(R.id.horario);
        caja_snAprobacion_marca = findViewById(R.id.caja_snAprobacion_marca);
        guardar_marca = findViewById(R.id.guardar_marca);
        marca_snAprobacion = findViewById(R.id.marca_snAprobacion);
        caja_snAprobacion_marca_final = findViewById(R.id.caja_snAprobacion_marca_final);
        cambiar_marca = findViewById(R.id.cambiar_marca);
        modelo_snAprobacion = findViewById(R.id.modelo_snAprobacion);
        modelo_snAprobacion_final = findViewById(R.id.modelo_snAprobacion_final);
        caja_snAprobacion_modelo_final = findViewById(R.id.caja_snAprobacion_modelo_final);
        guardar_modelo = findViewById(R.id.guardar_modelo);
        cambiar_modelo = findViewById(R.id.cambiar_modelo);
        cambiar_Codigomarca_Snaprobacion = findViewById(R.id.cambiar_Codigomarca_Snaprobacion);
        caja_Codigomarca_Snaprobacion_final = findViewById(R.id.caja_Codigomarca_Snaprobacion_final);
        CodigomarcaSnaprobacion = findViewById(R.id.CodigomarcaSnaprobacion);
        guardar_Codigomarca_Snaprobacion = findViewById(R.id.guardar_Codigomarca_Snaprobacion);
        Codigomarca_Snaprobacion_texto = findViewById(R.id.Codigomarca_Snaprobacion_texto);
        caja_Codigomarca_Snaprobacion = findViewById(R.id.caja_Codigomarca_Snaprobacion);


        caja_CodigomaModelo_Snaprobacion = findViewById(R.id.caja_CodigomaModelo_Snaprobacion);
        CodigomaModelo_Snaprobacion_texto = findViewById(R.id.CodigomaModelo_Snaprobacion_texto);
        guardar_CodigomaModelo_Snaprobacion = findViewById(R.id.guardar_CodigomaModelo_Snaprobacion);
        caja_CodigomaModelo_Snaprobacion_final = findViewById(R.id.caja_CodigomaModelo_Snaprobacion_final);
        CodigomaModeloSnaprobacion = findViewById(R.id.CodigomaModeloSnaprobacion);
        cambiar_CodigomaModelo_Snaprobacion = findViewById(R.id.cambiar_CodigomaModelo_Snaprobacion);


        caja_ano_Snaprobacion = findViewById(R.id.caja_ano_Snaprobacion);
        ano_Snaprobacion_texto = findViewById(R.id.ano_Snaprobacion_texto);
        guardar_ano_Snaprobacion = findViewById(R.id.guardar_ano_Snaprobacion);
        caja_ano_Snaprobacion_final = findViewById(R.id.caja_ano_Snaprobacion_final);
        anoSnaprobacion = findViewById(R.id.anoSnaprobacion);
        cambiar_ano_Snaprobacion = findViewById(R.id.cambiar_ano_Snaprobacion);
        caja_tipo_visita = findViewById(R.id.caja_tipo_visita);
        tipo_visita = findViewById(R.id.tipo_visita);




        caja_snAprobacion_modelo = findViewById(R.id.caja_snAprobacion_modelo);
        marca_snAprobacion_final = findViewById(R.id.marca_snAprobacion_final);
        caja_alcanceMinSnaprobacion = findViewById(R.id.caja_alcanceMinSnaprobacion);
        alcanceMinSnAprobacion_texto = findViewById(R.id.alcanceMinSnAprobacion_texto);
        guardar_alcanceMin_snAprobacion = findViewById(R.id.guardar_alcanceMin_snAprobacion);
        caja_alcanceSMinnaprobacion_final = findViewById(R.id.caja_alcanceSMinnaprobacion_final);
        alcanceMinSnAprobacion = findViewById(R.id.alcanceMinSnAprobacion);
        cambiar_alcanceMin_snAprobacion = findViewById(R.id.cambiar_alcanceMin_snAprobacion);


        tipo_visita= findViewById(R.id.tipo_visita);
        cons_factura= findViewById(R.id.cons_factura);
        cons_factura= findViewById(R.id.cons_factura);
        primera_si= findViewById(R.id.primera_si);
        primera_no= findViewById(R.id.primera_no);
        factura_si= findViewById(R.id.factura_si);
        factura_no= findViewById(R.id.factura_no);

        relacion_comercial_si= findViewById(R.id.relacion_comercial_si);
        relacion_comercial_no= findViewById(R.id.relacion_comercial_no);
        amenaza_integridad_si= findViewById(R.id.amenaza_integridad_si);
        amenaza_integridad_no= findViewById(R.id.amenaza_integridad_no);
        conflicto_intereses_si= findViewById(R.id.conflicto_intereses_si);
        conflicto_intereses_no= findViewById(R.id.conflicto_intereses_no);


        acta_dictamen_final= findViewById(R.id.acta_dictamen_final);
        caja_orden_tipoVerificacion= findViewById(R.id.caja_orden_tipoVerificacion);
        orden_tipoVerificacion= findViewById(R.id.orden_tipoVerificacion);
        caja_orden_merca= findViewById(R.id.caja_orden_merca);
        orden_merca= findViewById(R.id.orden_merca);
        caja_orden_modelo= findViewById(R.id.caja_orden_modelo);
        orden_modelo= findViewById(R.id.orden_modelo);
        caja_orden_numSerie= findViewById(R.id.caja_orden_numSerie);
        orden_numSerie= findViewById(R.id.orden_numSerie);
        caja_orden_alcanceMax= findViewById(R.id.caja_orden_alcanceMax);
        orden_alcanceMax= findViewById(R.id.orden_alcanceMax);
        caja_orden_eod= findViewById(R.id.caja_orden_eod);
        orden_eod= findViewById(R.id.orden_eod);
        caja_orden_alcanceMin= findViewById(R.id.caja_orden_alcanceMin);
        orden_alcanceMin= findViewById(R.id.orden_alcanceMin);
        caja_orden_modeloPrototipo= findViewById(R.id.caja_orden_modeloPrototipo);
        orden_modeloPrototipo= findViewById(R.id.orden_modeloPrototipo);
        caja_orden_claseExactitud= findViewById(R.id.caja_orden_claseExactitud);
        orden_claseExactitud= findViewById(R.id.orden_claseExactitud);
        caja_orden_alcanceMedicion= findViewById(R.id.caja_orden_alcanceMedicion);
        orden_alcanceMedicion= findViewById(R.id.orden_alcanceMedicion);




        SERVIDOR_CONTROLADOR = new Servidor().local;



        modeloSHER=getSharedPreferences("modelos",this.MODE_PRIVATE);
        editormodelo=modeloSHER.edit();
        editormodelo.putString("modelo","");
        editormodelo.apply();
        checkModel=modeloSHER.getString("modelo","no");



        idSher=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        id_usuer=idSher.getString("id","no");
        Log.e("ID",""+id_usuer);
        id_SesionSher=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        id_SesionUsuer=id_SesionSher.getString("id_sesion","no");
        Log.e("ID",""+id_SesionUsuer);

        almacen_basculas = findViewById(R.id.almacen_basculas);
        recycler_numero_basc = findViewById(R.id.recycler_numero_basc);
        caja_recycler_basculas = findViewById(R.id.caja_recycler_basculas);

        regresar_otravez_formulario = findViewById(R.id.regresar_otravez_formulario);
        agregar_otra_bascula = findViewById(R.id.agregar_otra_bascula);
        ir_preguntas = findViewById(R.id.ir_preguntas);
        ultimas_preguntas = findViewById(R.id.ultimas_preguntas);

        caja_integridad = findViewById(R.id.caja_integridad);
        amenaza_integridad_si = findViewById(R.id.amenaza_integridad_si);
        amenaza_integridad_no = findViewById(R.id.amenaza_integridad_no);
        cons_relacion_comercial = findViewById(R.id.cons_relacion_comercial);
        relacion_comercial_si = findViewById(R.id.relacion_comercial_si);
        relacion_comercial_no = findViewById(R.id.relacion_comercial_no);
        cons_conflicto_intereses = findViewById(R.id.cons_conflicto_intereses);
        conflicto_intereses_si = findViewById(R.id.conflicto_intereses_si);
        conflicto_intereses_no = findViewById(R.id.conflicto_intereses_no);




        ArrayAdapter<String> adaptador =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, APROBACION);
        autoAprobacion.setAdapter(adaptador);
        autoAprobacion.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
            public int findTokenStart(CharSequence text, int cursor) {
                int i = cursor;

                while (i > 0 && text.charAt(i - 1) != ' ') {
                    i--;
                }
                while (i < cursor && text.charAt(i) == ' ') {
                    i++;
                }

                return i;
            }

            public int findTokenEnd(CharSequence text, int cursor) {
                int i = cursor;
                int len = text.length();

                while (i < len) {
                    if (text.charAt(i) == ' ') {
                        return i;
                    } else {
                        i++;
                    }
                }

                return len;
            }

            public CharSequence terminateToken(CharSequence text) {
                int i = text.length();

                while (i > 0 && text.charAt(i - 1) == ' ') {
                    i--;
                }

                if (i > 0 && text.charAt(i - 1) == ' ') {
                    return text;
                } else {
                    if (text instanceof Spanned) {
                        SpannableString sp = new SpannableString(text + " ");
                        TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                                Object.class, sp, 0);
                        return sp;
                    } else {
                        return text + " ";
                    }
                }
            }
        });


        final int permisoLocacion = ContextCompat.checkSelfPermission(Mapa.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permisoLocacion != PackageManager.PERMISSION_GRANTED) {
            solicitarPermisoLocation();

            Log.e("paso", "paso");
        }

        convertidor = new CoordinateConverter();
        UTMPoint p = fromGeodeticToUTM(longitud, latitud);


        iniciar_verificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formulario_principal.setVisibility(View.VISIBLE);
                consultarFormaMedicamento();
                mapaid.setVisibility(View.GONE);
                caja_punto_partida.setVisibility(View.GONE);


            }
        });
        regresar_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                formulario_principal.setVisibility(view.GONE);
                mapaid.setVisibility(view.VISIBLE);
                caja_punto_partida.setVisibility(View.VISIBLE);
            }
        });


        guardar_nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nuevo_nombre = nombre_texto.getText().toString();
                nombre.setText(nuevo_nombre);
                if (!nuevo_nombre.trim().equals("")) {
                    caja_edit_nombre.setVisibility(View.GONE);
                    caja_nombre_final.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "El nombre es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_nombre_final.setVisibility(view.GONE);
                caja_edit_nombre.setVisibility(view.VISIBLE);
            }
        });
        guardar_rfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nuevo_rfc = rfc_texto.getText().toString();
                rfc.setText(nuevo_rfc);

                if (!nuevo_rfc.trim().equals("")&&nuevo_rfc!=null) {
                    caja_rfc_final.setVisibility(View.VISIBLE);
                    caja_edit_rfc.setVisibility(View.GONE);
                    /*String regexFecha ="[a-zA-Z]+[0-9]+-[a-zA-Z]+";
                    Pattern pattern = Pattern.compile(regexFecha);
                    Matcher matcher = pattern.matcher(nuevo_rfc);*/
                 /*   if(matcher.matches()==true){

                        rfc_existoso=true;


                    }
                    else {
                        Toast.makeText(getApplicationContext(), "El formato oficial de rfc es necesario.", Toast.LENGTH_LONG).show();
                    }*/

                }
                else {
                    Toast.makeText(getApplicationContext(), "el rfc es necesario.", Toast.LENGTH_LONG).show();
                }

            }
        });
        cambiar_rfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_rfc_final.setVisibility(view.GONE);
                caja_edit_rfc.setVisibility(view.VISIBLE);
            }
        });
        guardar_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cuenta = tel_texto.getText().toString().trim().length();
                if (cuenta == 10) {
                    tel10 = true;
                } else {
                    tel10 = false;
                }
                nuevo_tel = tel_texto.getText().toString();
                telefono.setText(nuevo_tel);
                if (!nuevo_tel.trim().equals("")) {
                    if (tel10 == true) {


                        caja_edit_tel.setVisibility(View.GONE);
                        caja_tel_final.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "El telefono debe ser de 10 digitos.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "El telefono es necesario.", Toast.LENGTH_LONG).show();
                }

            }
        });
        cambiar_telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_tel_final.setVisibility(view.GONE);
                caja_edit_tel.setVisibility(view.VISIBLE);
            }
        });


        String date = new SimpleDateFormat("dd-MM-yyyy",
                Locale.getDefault()).format(new Date());
        Log.e("fecha", "" + date);
        fecha.setText(date);
        guardar_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nueva_fecha = fecha.getText().toString();
                fecha_final.setText(nueva_fecha);
                nueva_fecha_final= fecha_final.getText().toString();
                if (!nueva_fecha.trim().equals("")&&nueva_fecha!=null) {

                    String regexFecha ="[0-9]{2}-[0-9]{2}-[0-9]{4}";
                    Pattern pattern = Pattern.compile(regexFecha);
                    Matcher matcher = pattern.matcher(nueva_fecha);
                    if(matcher.matches()==true){

                        fecha_existoso=true;
                        caja_fecha_final.setVisibility(View.VISIBLE);
                        caja_edit_fecha.setVisibility(View.GONE);

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "El formato de fecha es necesario 99-99-9999 es necesario.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "La formato es necesario.", Toast.LENGTH_LONG).show();
                }

            }
        });
        cambiar_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_fecha_final.setVisibility(view.GONE);
                caja_edit_fecha.setVisibility(view.VISIBLE);
            }
        });
        /*fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {
                if(!tieneFoco)
                {
                    fecha_final_Str=fecha.getText().toString().trim().toLowerCase();
                    if (!fecha_final_Str.equals("")&&fecha_final_Str!=null)
                    {
                        // String regex = "^(.+)@(.+)$";
                        String regexUsuario = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                        Pattern pattern = Pattern.compile(regexUsuario);
                        Matcher matcher = pattern.matcher(fecha_final_Str);
                        if(matcher.matches()==true){
                            correo_exitoso=true;
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Ingrese correo valido.",Toast.LENGTH_LONG).show();
                }
            }
        });*/




        adapterGiro = new AdapterGiro(activity, R.layout.lista_giro, listaGiro, getResources());
        giros.setAdapter(adapterGiro);











        /*adapterCantidadBasculas=new AdapterCantidadBasculas(activity,R.layout.item6,listaCantidadBasc,getResources());
        recycler_numero_basc.setAdapter(adapterCantidadBasculas);
*/
        guardar_alcance_snAprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nueva_serie = alcanceSnAprobacion_texto.getText().toString();
                alcanceSnAprobacion.setText(nueva_serie);
                if (!nueva_serie.trim().equals("")) {
                    caja_alcanceSnaprobacion.setVisibility(View.GONE);
                    caja_alcanceSnaprobacion_final.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "El alcance max es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_alcance_snAprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_alcanceSnaprobacion_final.setVisibility(view.GONE);
                caja_alcanceSnaprobacion.setVisibility(view.VISIBLE);
            }
        });
        guardar_costo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nuevo_costo = costo_texto.getText().toString();
                costo.setText("$"+nuevo_costo);
                if (!nuevo_costo.trim().equals("")) {
                    caja_edit_costo.setVisibility(View.GONE);
                    caja_costo_final.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "El costo es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_costo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_costo_final.setVisibility(view.GONE);
                caja_edit_costo.setVisibility(view.VISIBLE);
            }
        });
        guardar_numSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nuevo_numSerie = numSerie_texto.getText().toString();
                numSerie.setText(nuevo_numSerie);
                if (!nuevo_numSerie.trim().equals("")) {
                    caja_edit_numSerie.setVisibility(View.GONE);
                    caja_numSerie_final.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "El numero de serie es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_serie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_numSerie_final.setVisibility(view.GONE);
                caja_edit_numSerie.setVisibility(view.VISIBLE);
            }
        });
        regresar_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                formulario_principal.setVisibility(view.VISIBLE);
                formulario_bascula.setVisibility(view.GONE);

            }
        });
        finalizar_reg_bascula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_final="";
                cuenta_basculas=0;
                for (int i=0; i<listaCantidadBasc.size();i++){
                    enviar_numero_aprobacion=listaCantidadBasc.get(i).getCantidad_numero_aprobacion();
                    Log.e("lista_Array1",""+enviar_numero_aprobacion);
                    enviar_marca=listaCantidadBasc.get(i).getCantidad_marca();
                    Log.e("lista_Array1",""+enviar_marca);
                    enviar_modelo=listaCantidadBasc.get(i).getCantidad_modelo();
                    Log.e("lista_Array1",""+enviar_modelo);
                    enviar_AlcanceMax=listaCantidadBasc.get(i).getCantidad_AlcanceMax();
                    Log.e("lista_Array1",""+enviar_AlcanceMax);
                    enviar_AlcanceMin=listaCantidadBasc.get(i).getCantidad_AlcanceMin();
                    Log.e("lista_Array1",""+enviar_AlcanceMin);
                    enviar_CodigoMarca=listaCantidadBasc.get(i).getCantidad_CodigoMarca();
                    Log.e("lista_Array1",""+enviar_CodigoMarca);
                    enviar_CodigoModelo=listaCantidadBasc.get(i).getCantidad_CodigoModelo();
                    Log.e("lista_Array1",""+enviar_CodigoModelo);
                    enviar_anoAprobacion=listaCantidadBasc.get(i).getCantidad_anoAprobacion();
                    Log.e("lista_Array1",""+enviar_anoAprobacion);
                    enviar_eod=listaCantidadBasc.get(i).getCantidad_eod();
                    Log.e("lista_Array1",""+enviar_eod);
                    enviar_TipoInstrumento=listaCantidadBasc.get(i).getCantidad_TipoInstrumento();
                    Log.e("lista_Array1",""+enviar_TipoInstrumento);
                    enviar_claseExactitud=listaCantidadBasc.get(i).getCantidad_claseExactitud();
                    Log.e("lista_Array1",""+enviar_claseExactitud);
                    enviar_marcoPesas=listaCantidadBasc.get(i).getCantidad_marcoPesas();
                    Log.e("lista_Array1",""+enviar_marcoPesas);
                    enviar_pesas5kg=listaCantidadBasc.get(i).getCantidad_pesas5kg();
                    Log.e("lista_Array1",""+enviar_pesas5kg);
                    enviar_pesas10kg=listaCantidadBasc.get(i).getCantidad_pesas10kg();
                    Log.e("lista_Array1",""+enviar_pesas10kg);
                    enviar_pesas20kg=listaCantidadBasc.get(i).getCantidad_pesas20kg();
                    Log.e("lista_Array1",""+enviar_pesas20kg);
                    enviar_pesaClase_exactitud=listaCantidadBasc.get(i).getCantidad_pesaClase_exactitud();
                    Log.e("lista_Array1",""+enviar_pesaClase_exactitud);
                    enviar_horario=listaCantidadBasc.get(i).getCantidad_horario();
                    Log.e("lista_Array1",""+enviar_horario);
                    enviar_TipoVisita=listaCantidadBasc.get(i).getCantidad_TipoVisita();
                    Log.e("lista_Array1",""+enviar_TipoVisita);
                    enviar_costo=listaCantidadBasc.get(i).getCantidad_costo();
                    Log.e("lista_Array1",""+enviar_costo);
                    enviar_numSerie=listaCantidadBasc.get(i).getCantidad_numeroSerie();
                    Log.e("lista_Array1",""+enviar_numSerie);
                    cuenta_basculas++;
                    String str_tmp=enviar_numero_aprobacion+" /*-*/ "+enviar_marca+" /*-*/ "+enviar_modelo+" /*-*/ "+enviar_AlcanceMax
                            +" /*-*/ "+enviar_AlcanceMin+" /*-*/ "+enviar_CodigoMarca+" /*-*/ "+enviar_CodigoModelo+
                            " /*-*/ "+enviar_anoAprobacion+" /*-*/ "+enviar_eod+" /*-*/ "+enviar_TipoInstrumento+" /*-*/ "+
                            enviar_claseExactitud+" /*-*/ "+enviar_marcoPesas+" /*-*/ "+enviar_pesas5kg+
                            " /*-*/ "+enviar_pesas10kg+" /*-*/ "+enviar_pesas20kg+" /*-*/ "+enviar_pesaClase_exactitud+" /*-*/ "+
                            enviar_horario+" /*-*/ "+enviar_TipoVisita+" /*-*/ "+enviar_costo+
                            " /*-*/ "+enviar_numSerie;
                    Log.e("topiteishon",""+str_tmp);
                    if(str_final.equals("")){
                        str_final=str_tmp;
                    }
                    else{
                        str_final=str_final+" /*??*/ "+str_tmp;
                    }
                }
                Log.e("numero_basc",""+cuenta_basculas);
                Log.e("final",""+str_final);

                formulario_bascula.setVisibility(view.GONE);
                caja_finalizar_basc.setVisibility(view.VISIBLE);


            }
        });
        ir_preguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                almacen_basculas.setVisibility(view.GONE);
                ultimas_preguntas.setVisibility(View.VISIBLE);
            }
        });
        finalizar_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja_finalizar_basc.setVisibility(view.GONE);
                formulario_bascula.setVisibility(View.VISIBLE);
            }
        });
        finalizar_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                asincronaEnviarBasculas=new AsincronaEnviarBasculas();
                asincronaEnviarBasculas.execute();
                caja_mensaje_basc.setVisibility(view.GONE);
                caja_finalizar_basc.setVisibility(view.GONE);
                ultimas_preguntas.setVisibility(view.GONE);
                acta_dictamen_final.setVisibility(view.VISIBLE);
          /*      orden_tipoVerificacion.setText(enviar_TipoVisita);
                orden_merca.setText(enviar_marca);
                orden_modelo.setText(enviar_modelo);
                orden_numSerie.setText(enviar_numSerie);
                orden_alcanceMax.setText(enviar_AlcanceMax);
                orden_eod.setText(enviar_eod);
                orden_alcanceMin.setText(enviar_AlcanceMin);
                orden_modeloPrototipo.setText(enviar_numero_aprobacion);
                orden_claseExactitud.setText(enviar_claseExactitud);
                orden_alcanceMedicion.setText(nuevo_alcanceMedicion);;*/


                //mapaid.setVisibility(view.VISIBLE);
                   int height_tmp=acta_dictamen_final.getHeight();
                   Log.e("height!!!!!!",height_tmp+"!!!");


            }
        });
        giros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listas_giro = findViewById(R.id.listaGiro);
                if (listas_giro == null) {
                    listas_giro = (TextView) view.findViewById(R.id.listaGiro);
                } else {
                    listas_giro = (TextView) view.findViewById(R.id.listaGiro);
                }
                seleccion_giro = listas_giro.getText().toString();
                Log.e("tipogiro", "" + seleccion_giro);


                Log.e("tipo", "" + seleccion_giro);
                Log.e("tipo", "" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        valorCheckboxPrimera="";
        //inicial.setChecked(true);
        primera_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco();
                primera_si.setChecked(true);
                valorCheckboxPrimera="primera_si";
                if(valorCheckboxPrimera.equals("inicial")){

                    tipo_visita.setText("Inicial");
                }
                Log.e("cambios", "" + valorCheckboxPrimera);
            }
        });
        primera_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco();
                primera_no.setChecked(true);
                tipo_visita.setText("");
                valorCheckboxPrimera="primera_no";
                Log.e("cambios", "" + valorCheckboxPrimera);
            }
        });
        valorCheckboxFactura="";
        //inicial.setChecked(true);
        factura_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco2();
                factura_si.setChecked(true);
                valorCheckboxFactura="factura_si";
                Log.e("cambios", "" + valorCheckboxFactura);

            }
        });
        factura_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco2();
                factura_no.setChecked(true);
                valorCheckboxFactura="factura_no";
                Log.e("cambios", "" + valorCheckboxFactura);
                if(valorCheckboxPrimera.equals("primera_si")&&valorCheckboxFactura.equals("factura_no"))
                {


                    mapaid.setVisibility(View.VISIBLE);
                }



            }
        });
        valorCheckboxComercial="";
        relacion_comercial_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco3();
                relacion_comercial_si.setChecked(true);
                valorCheckboxComercial="relacion_comercial_si";

                Log.e("cambios", "" + valorCheckboxComercial);
            }
        });
        relacion_comercial_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco3();
                relacion_comercial_no.setChecked(true);
                valorCheckboxComercial="relacion_comercial_no";
                Log.e("cambios", "" + valorCheckboxComercial);
            }
        });
        valorCheckboxIntegridad="";
        //inicial.setChecked(true);
        amenaza_integridad_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco4();
                amenaza_integridad_si.setChecked(true);
                valorCheckboxIntegridad="amenaza_si";
                Log.e("cambios", "" + valorCheckboxFactura);

            }
        });
        amenaza_integridad_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco4();
                amenaza_integridad_no.setChecked(true);
                valorCheckboxIntegridad="amenaza_no";
                Log.e("cambios", "" + valorCheckboxFactura);

            }
        });
        valorCheckboxIntereses="";
        //inicial.setChecked(true);

        conflicto_intereses_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco5();
                conflicto_intereses_si.setChecked(true);
                valorCheckboxIntereses="intereses_si";
                Log.e("cambios", "" + valorCheckboxFactura);

            }
        });
        conflicto_intereses_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco5();
                conflicto_intereses_no.setChecked(true);
                valorCheckboxIntereses="intereses_no";
                Log.e("cambios", "" + valorCheckboxFactura);

            }
        });
        guardar_mercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nuevo_mercado = mercado_texto.getText().toString();
                mercado_vista.setText(nuevo_mercado);
                if (!nuevo_mercado.trim().equals("")) {
                    caja_mercado.setVisibility(View.GONE);
                    caja_mercado_final.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "El costo es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_mercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_mercado_final.setVisibility(view.GONE);
                caja_mercado.setVisibility(view.VISIBLE);
            }
        });



        siguiente_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cuenta = tel_texto.getText().toString().trim().length();
                if (cuenta == 10) {
                    tel10 = true;
                } else {
                    tel10 = false;
                }
                nuevo_nombre = nombre_texto.getText().toString();
                nuevo_tel = tel_texto.getText().toString();
                nueva_fecha = fecha.getText().toString();
                nueva_fecha_final= fecha_final.getText().toString();
                if(!nueva_fecha_final.trim().equals("")){
                    if (!nuevo_nombre.trim().equals("")) {
                        if(!seleccion_giro.trim().equals("Giro")){
                            if(!nuevo_mercado.trim().equals("")){
                                if (!nuevo_tel.trim().equals("")) {

                                    if (tel10==true){
                                        formulario_principal.setVisibility(view.GONE);
                                        formulario_bascula.setVisibility(view.VISIBLE);

                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "El telefono debe ser de 10 digitos.", Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Seleccionar mercado es necesario.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Seleccionar giro es necesario.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "El nombre es necesario.", Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Definir la fecha es necesario.", Toast.LENGTH_LONG).show();
                }


            }
        });



        guardar_aprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conexion3=new Conexion(getApplicationContext(),"basculas",null,1);
                database3=conexion3.getReadableDatabase();
                nueva_aprobacion = autoAprobacion.getText().toString();
                aprobacion_basc.setText(nueva_aprobacion);
                String[] fecha_fragmentada=nueva_fecha.split("-");
                String elementoMes=fecha_fragmentada[1];
                String elementoAno=fecha_fragmentada[2];
                elementoNumeroMes= Integer.parseInt(elementoMes);
                elementoNumerAno= Integer.parseInt(elementoAno);
                Log.e("mes_chems",""+elementoMes);
                Log.e("mes_chems1",""+elementoNumeroMes);
                Log.e("mes_chems1",""+elementoAno);




                try {
                    String[] parametros = {nueva_aprobacion.trim()};
                    Log.e("nueva_aprobacion",""+nueva_aprobacion);
                    cursor3= database3.rawQuery("SELECT DISTINCT numero_aprobacion,marca,modelo FROM basculas WHERE numero_aprobacion=?",parametros);

                    int cuenta =cursor3.getCount();
                    Log.e("modelo_cuenta",""+cuenta);
                    if(cuenta==0){
                        Log.e("topitas_cuenta",""+cuenta);
                        Log.e("errodecuenta",""+elementoNumeroMes);
                        if ( elementoNumeroMes<4&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_no")){
                            tipo_visita.setText("1er semestre");
                            Log.e("adentro de topita1",""+elementoNumeroMes);

                        }
                        else{
                            if( elementoNumeroMes>3&&elementoNumeroMes<7&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_no")){
                                tipo_visita.setText("1er semestre extraordinarias");
                                Log.e("adentro de topita2",""+tipo_visita);
                            }
                            else {
                                if(elementoNumeroMes>6&&elementoNumeroMes<10&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_no")){
                                    tipo_visita.setText("2do semestre");
                                    Log.e("adentro de topita3",""+tipo_visita);

                                }
                                else {
                                    if(elementoNumeroMes>9&&elementoNumeroMes==12&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_no")){
                                        tipo_visita.setText("2do semestre Extraordinarias o iniciales");
                                        Log.e("adentro de topita4",""+tipo_visita);
                                    }
                                }
                            }
                        }
                        if(valorCheckboxPrimera.equals("primera_si")&&valorCheckboxFactura.equals("factura_si")){
                            tipo_visita.setText("Inicial");
                        }

                        Log.e("adentro de topita",""+cuenta);

                        tipo_camino=1;
                        caja_snAprobacion_marca.setVisibility(View.VISIBLE);
                        caja_recycler_marca.setVisibility(View.GONE);
                        caja_snAprobacion_modelo.setVisibility(View.VISIBLE);
                        caja_recycler_modelo.setVisibility(View.GONE);
                        caja_recycler_alcance.setVisibility(View.GONE);
                        caja_alcanceSnaprobacion.setVisibility(View.VISIBLE);
                        caja_recycler_minimo.setVisibility(View.GONE);
                        caja_alcanceMinSnaprobacion.setVisibility(View.VISIBLE);
                        caja_codigo_marca.setVisibility(View.GONE);
                        caja_Codigomarca_Snaprobacion.setVisibility(View.VISIBLE);
                        caja_codigo_modelo.setVisibility(View.GONE);
                        caja_CodigomaModelo_Snaprobacion.setVisibility(View.VISIBLE);
                        caja_ano_aprobacion.setVisibility(View.GONE);
                        caja_ano_Snaprobacion.setVisibility(View.VISIBLE);
                    }
                    else{
                        caja_snAprobacion_marca.setVisibility(View.GONE);
                        caja_recycler_marca.setVisibility(View.VISIBLE);
                        caja_snAprobacion_modelo.setVisibility(View.GONE);
                        caja_recycler_modelo.setVisibility(View.VISIBLE);
                        caja_snAprobacion_modelo.setVisibility(View.GONE);
                        caja_recycler_alcance.setVisibility(View.VISIBLE);
                        caja_alcanceSnaprobacion.setVisibility(View.GONE);
                        caja_recycler_minimo.setVisibility(View.VISIBLE);
                        caja_alcanceMinSnaprobacion.setVisibility(View.GONE);
                        caja_codigo_marca.setVisibility(View.VISIBLE);
                        caja_Codigomarca_Snaprobacion.setVisibility(View.GONE);
                        caja_codigo_modelo.setVisibility(View.VISIBLE);
                        caja_CodigomaModelo_Snaprobacion.setVisibility(View.GONE);
                        caja_ano_aprobacion.setVisibility(View.VISIBLE);
                        caja_ano_Snaprobacion.setVisibility(View.GONE);
                        while (cursor3.moveToNext()){
                            Log.e("marca",cursor3.getString(1));
                            recycler_marca.setText(cursor3.getString(1));
                            nueva_marca = recycler_marca.getText().toString();
                            Log.e("marca",""+nueva_marca);

                            try {
                                String[] parametros2 = {nueva_aprobacion.trim(),nueva_marca.trim()};
                                Log.e("parametros2",""+parametros2);
                                cursor5= database3.rawQuery("SELECT DISTINCT numero_aprobacion,marca,modelo,codigo_marca,codigo_modelo,ano_aprobacion FROM basculas WHERE numero_aprobacion=? AND marca=? ",parametros2);
                                Log.e("modelo",""+cursor5);

                                int cuenta2 =cursor5.getCount();
                                Log.e("modelo_cuenta",""+cuenta2);
                                listaModelo.clear();
                                while (cursor5.moveToNext()){
                                    Log.e("modelo",cursor5.getString(2));
                                    tipo_camino=2;
                                    listaModelo.add(new ModeloRecycler(cursor5.getString(2)));
                                    codigo_marca.setText(cursor5.getString(3));
                                    codigo_modelo.setText(cursor5.getString(4));
                                    ano_aprobacion.setText(cursor5.getString(5));
                                    ano_global=cursor5.getString(5);
                                    nuevo_coodigoMarca=cursor5.getString(3);
                                    nuevo_coodigoModelo=cursor5.getString(4);

                                    numero_anoglobal= Integer.parseInt(ano_global);
                                    Log.e("numero globals",""+numero_anoglobal);
                                    //Log.e("lista_basculas",""+BASCULAS);
                                }
                                setListaModelo();
                                resta_anos=elementoNumerAno-numero_anoglobal;
                                Log.e("año_de_aproabacion",""+resta_anos);

                                if(valorCheckboxPrimera.equals("primera_si")&&valorCheckboxFactura.equals("factura_si")){

                                    tipo_visita.setText("Inicial");
                                }


                                if(valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_si")&&resta_anos<10&&elementoNumeroMes<7){
                                    tipo_visita.setText("Periodica anual");
                                }
                                if(valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_si")&&resta_anos<10&&elementoNumeroMes>6){
                                    tipo_visita.setText("Anual Extraordinaria");
                                }

                                if ( elementoNumeroMes<4&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_no")&&resta_anos>10){
                                    tipo_visita.setText("1er semestre");
                                    Log.e("adentro de topita1",""+elementoNumeroMes);

                                }
                                else{
                                    if( elementoNumeroMes>3&&elementoNumeroMes<7&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_no")){
                                        tipo_visita.setText("1er semestre extraordinarias");
                                        Log.e("adentro de topita2",""+tipo_visita);
                                    }
                                    else {
                                        if(elementoNumeroMes>6&&elementoNumeroMes<10&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_no")){
                                            tipo_visita.setText("2do semestre");
                                            Log.e("adentro de topita3",""+tipo_visita);

                                        }
                                        else {
                                            if(elementoNumeroMes>9&&elementoNumeroMes==12&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_no")){
                                                tipo_visita.setText("2do semestre Extraordinarias o iniciales");
                                                Log.e("adentro de topita4",""+tipo_visita);
                                            }
                                        }
                                    }
                                }
                                if ( elementoNumeroMes<4&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_si")&&resta_anos>10){
                                    tipo_visita.setText("1er semestre");
                                    Log.e("adentro de topita1",""+elementoNumeroMes);

                                }
                                else{
                                    if( elementoNumeroMes>3&&elementoNumeroMes<7&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_si")&&resta_anos>10){
                                        tipo_visita.setText("1er semestre extraordinarias");
                                        Log.e("adentro de topita2",""+tipo_visita);
                                    }
                                    else {
                                        if(elementoNumeroMes>6&&elementoNumeroMes<10&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_si")&&resta_anos>10){
                                            tipo_visita.setText("2do semestre");
                                            Log.e("adentro de topita3",""+tipo_visita);

                                        }
                                        else {
                                            if(elementoNumeroMes>9&&elementoNumeroMes==12&&valorCheckboxPrimera.equals("primera_no")&&valorCheckboxFactura.equals("factura_si")&&resta_anos>10){
                                                tipo_visita.setText("2do semestre Extraordinarias o iniciales");
                                                Log.e("adentro de topita4",""+tipo_visita);
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e){
                                Log.e("basculas","no existe");
                            }


                            //Log.e("lista_basculas",""+BASCULAS);
                        }

                    }


                    Log.e("lista_basculas",""+cursor2);
                }
                catch (Exception e){
                    Log.e("basculas","no existe");
                }
                if (!nueva_aprobacion.trim().equals("")) {
                    caja_auto_aproba.setVisibility(View.GONE);
                    caja_aprobacion_final.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "La marca es necesaria.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_aprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_aprobacion_final .setVisibility(view.GONE);
                caja_auto_aproba.setVisibility(view.VISIBLE);
            }
        });


        agregar_bascula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.e("tipo_camino",""+tipo_camino);
                if(tipo_camino==2){
                    nueva_aprobacion = autoAprobacion.getText().toString();
                    nueva_marca = recycler_marca.getText().toString();
                    checkModel=modeloSHER.getString("modelo","no");
                    nuevo_alcanceMax_aprobado_str=nuevo_alcanceMax_aprobado;
                    if(nuevo_alcanceMax_aprobado.equals(" ")){
                        nuevo_alcanceMax_aprobado_str=alcanceSnAprobacion_texto.getText().toString();
                        Log.e("valor_cambiado",""+nuevo_alcanceMax_aprobado_str);
                    }
                    nuevo_alcanceMin_aprobado_str=nuevo_alcanceMin_aprobado;
                    if(nuevo_alcanceMin_aprobado.equals(" ")){
                        nuevo_alcanceMin_aprobado_str=alcanceMinSnAprobacion_texto.getText().toString();
                        Log.e("valor_cambiadoMin",""+nuevo_alcanceMin_aprobado_str);
                        nuevo_eod_str=eodSnaprobacion;
                        Log.e("valor_cambiadoMin",""+nuevo_alcanceMin_aprobado_str);
                        nuevo_tipoInstrumento_string=tipoInstrumentoSnaprobacion;
                        Log.e("instrumento_aqu",""+nuevo_tipoInstrumento_string);
                        Log.e("instrumento_a222222",""+tipoInstrumentoSnaprobacion);
                        nueva_claseExactitud_string=claseExactitudSnaprobacion;
                        nuevo_marco_pesas_string=marco_pesasSnaprobacion;
                        nuevo_pesas_5kg_string=pesas_5kgSnaprobacion;
                        nuevo_pesas_10kg_string=pesas_10kgSnaprobacion;
                        nuevo_pesas_20kg_string=pesas_20kgSnaprobacion;
                        nuevo_pesa_clase_exactitud_string= esa_clase_exactitudSnaprobacion;
                        nuevo_horario_string=horarioSnaprobacion;
                    }
                    else{
                        nuevo_eod_str=nuevo_eod;
                        nuevo_tipoInstrumento_string=nuevo_tipoInstrumento;
                        Log.e("instrumento_aqu",""+nuevo_tipoInstrumento_string);
                        Log.e("instrumento_a222222",""+nuevo_tipoInstrumento);
                        nueva_claseExactitud_string=nueva_claseExactitud;
                        nuevo_marco_pesas_string=nuevo_marco_pesas;
                        nuevo_pesas_5kg_string=nuevo_pesas_5kg;
                        nuevo_pesas_10kg_string=nuevo_pesas_10kg;
                        nuevo_pesas_20kg_string=nuevo_pesas_20kg;
                        nuevo_pesa_clase_exactitud_string= nuevo_pesa_clase_exactitud;
                        nuevo_horario_string=nuevo_horario;
                    }
                    nuevo_coodigoMarca_string=nuevo_coodigoMarca;
                    nuevo_coodigoModelo_string=nuevo_coodigoModelo;
                    ano_global_string=ano_global;

                    tipo_visita_string=tipo_visita.getText().toString();
                    nuevo_costo = costo_texto.getText().toString();
                    nuevo_numSerie = numSerie_texto.getText().toString();

                }
                else{
                    nueva_aprobacion = autoAprobacion.getText().toString();
                    nueva_marca = marca_snAprobacion.getText().toString();;
                    checkModel=modelo_snAprobacion.getText().toString();;
                    nuevo_alcanceMax_aprobado_str=alcanceSnAprobacion_texto.getText().toString();
                    nuevo_alcanceMin_aprobado_str=alcanceMinSnAprobacion_texto.getText().toString();;
                    nuevo_coodigoMarca_string=Codigomarca_Snaprobacion_texto.getText().toString();;
                    nuevo_coodigoModelo_string=CodigomaModelo_Snaprobacion_texto.getText().toString();;
                    ano_global_string=ano_Snaprobacion_texto.getText().toString();;
                    nuevo_eod_str=eodSnaprobacion;
                    nuevo_tipoInstrumento_string=tipoInstrumentoSnaprobacion;
                    Log.e("instrumento_aqu",""+nuevo_tipoInstrumento_string);
                    Log.e("instrumento_a222222",""+tipoInstrumentoSnaprobacion);
                    nueva_claseExactitud_string=claseExactitudSnaprobacion;
                    nuevo_marco_pesas_string=marco_pesasSnaprobacion;
                    nuevo_pesas_5kg_string=pesas_5kgSnaprobacion;
                    nuevo_pesas_10kg_string=pesas_10kgSnaprobacion;
                    nuevo_pesas_20kg_string=pesas_20kgSnaprobacion;
                    nuevo_pesa_clase_exactitud_string= esa_clase_exactitudSnaprobacion;
                    nuevo_horario_string=horarioSnaprobacion;
                    tipo_visita_string=tipo_visita.getText().toString();
                    nuevo_costo = costo_texto.getText().toString();
                    nuevo_numSerie = numSerie_texto.getText().toString();

                }


                if(!nueva_aprobacion.trim().equals("")){
                    if(!nueva_marca.trim().equals("")){
                        if (!checkModel.trim().equals("")){
                            if(!nuevo_alcanceMax_aprobado_str.trim().equals("")){
                                if(!nuevo_alcanceMin_aprobado_str.trim().equals("")){
                                    if(!nuevo_coodigoMarca_string.trim().equals("")){
                                        if(!nuevo_coodigoModelo_string.trim().equals("")){
                                            if(!ano_global_string.trim().equals("")){
                                                if(!nuevo_eod_str.trim().equals("")){
                                                    if(!nuevo_tipoInstrumento_string.trim().equals("")){
                                                        if(!nueva_claseExactitud_string.trim().equals("")){
                                                            if(!nuevo_marco_pesas_string.trim().equals("")){
                                                                if(!nuevo_pesas_5kg_string.trim().equals("")){
                                                                    if(!nuevo_pesas_10kg_string.trim().equals("")){
                                                                        if(!nuevo_pesas_20kg_string.trim().equals("")){
                                                                            if(!nuevo_pesa_clase_exactitud_string.trim().equals("")){
                                                                                if(!nuevo_horario_string.trim().equals("")){
                                                                                    if(!tipo_visita_string.trim().equals("")){
                                                                                        if(!nuevo_costo.trim().equals("")){
                                                                                            if(!nuevo_numSerie.trim().equals("")){
                                                                                                formulario_bascula.setVisibility(View.GONE);
                                                                                                almacen_basculas.setVisibility(View.VISIBLE);
                                                                                                Log.e("prueba1",""+nueva_aprobacion);
                                                                                                Log.e("prueba2",""+nueva_marca);
                                                                                                Log.e("prueba3",""+checkModel);
                                                                                                Log.e("prueba4",""+nuevo_alcanceMax_aprobado_str);
                                                                                                JSONObject jsonObject=new JSONObject();
                                                                                                json_datos_bascula=new JSONArray();
                                                                                                try {
                                                                                                    jsonObject.put("numero_aprobacion",nueva_aprobacion);
                                                                                                    jsonObject.put("marca",nueva_marca);
                                                                                                    jsonObject.put("modelo",checkModel);
                                                                                                    jsonObject.put("Alcance_max",nuevo_alcanceMax_aprobado_str);
                                                                                                    jsonObject.put("Alcance_min",nuevo_alcanceMin_aprobado_str);
                                                                                                    jsonObject.put("codigo_marca",nuevo_coodigoMarca_string);
                                                                                                    jsonObject.put("codigo_modelo",nuevo_coodigoModelo_string);
                                                                                                    jsonObject.put("ano_modelo",ano_global_string);
                                                                                                    jsonObject.put("eod",nuevo_eod_str);
                                                                                                    jsonObject.put("tipo_instrumento",nuevo_tipoInstrumento_string);
                                                                                                    jsonObject.put("clase_exactitud",nueva_claseExactitud_string);
                                                                                                    jsonObject.put("marco_pesas",nuevo_marco_pesas_string);
                                                                                                    jsonObject.put("pesas_5kg",nuevo_pesas_5kg_string);
                                                                                                    jsonObject.put("pesas_10kg",nuevo_pesas_10kg_string);
                                                                                                    jsonObject.put("pesas_20kg",nuevo_pesas_20kg_string);
                                                                                                    jsonObject.put("pesa_clase_exactitud",nuevo_pesa_clase_exactitud_string);
                                                                                                    jsonObject.put("horario",nuevo_horario_string);
                                                                                                    jsonObject.put("tipo_visita",tipo_visita_string);
                                                                                                    jsonObject.put("costo",nuevo_costo);
                                                                                                    jsonObject.put("numero_serie",nuevo_numSerie);
                                                                                                    json_datos_bascula.put(jsonObject);

                                                                                                    Log.e("1", String.valueOf(jsonObject));
                                                                                                    Log.e("2", String.valueOf(json_datos_bascula));
                                                                                                    for (int i=0; i<json_datos_bascula.length();i++)
                                                                                                    {
                                                                                                        try { JSONObject jsonSacando= json_datos_bascula.getJSONObject(i);
                                                                                                            String strAprobacion=jsonSacando.get("numero_aprobacion").toString();
                                                                                                            String strMarca=jsonSacando.get("marca").toString();
                                                                                                            String strModelo=jsonSacando.get("modelo").toString();
                                                                                                            String strAlcance_max=jsonSacando.get("Alcance_max").toString();
                                                                                                            String strAlcance_min=jsonSacando.get("Alcance_min").toString();
                                                                                                            String strCodigo_marca=jsonSacando.get("codigo_marca").toString();
                                                                                                            String strCodigo_modelo=jsonSacando.get("codigo_modelo").toString();
                                                                                                            String strAno_modelo=jsonSacando.get("ano_modelo").toString();
                                                                                                            String strEod=jsonSacando.get("eod").toString();
                                                                                                            String stTipo_instrumento=jsonSacando.get("tipo_instrumento").toString();
                                                                                                            String strClase_exactitud=jsonSacando.get("clase_exactitud").toString();
                                                                                                            String strMarco_pesas=jsonSacando.get("marco_pesas").toString();
                                                                                                            String strPesas_5kg=jsonSacando.get("pesas_5kg").toString();
                                                                                                            String stPesas_10kg=jsonSacando.get("pesas_10kg").toString();
                                                                                                            String strPesas_20kg=jsonSacando.get("pesas_20kg").toString();
                                                                                                            String strPesa_clase_exactitud=jsonSacando.get("pesa_clase_exactitud").toString();
                                                                                                            String strHorario=jsonSacando.get("horario").toString();
                                                                                                            String strTipoVisita=jsonSacando.get("tipo_visita").toString();
                                                                                                            String strCosto=jsonSacando.get("costo").toString();
                                                                                                            String strNumSerie=jsonSacando.get("numero_serie").toString();


                                                                                                            //listaCantidadBasc.clear();

                                                                                                            listaCantidadBasc.add(new CantidadBasculasRecycler(strAprobacion,strMarca,strModelo,strAlcance_max,strAlcance_min,
                                                                                                                    strCodigo_marca,strCodigo_modelo,strAno_modelo,strEod,stTipo_instrumento,
                                                                                                                    strClase_exactitud,strMarco_pesas,strPesas_5kg,stPesas_10kg,strPesas_20kg,strPesa_clase_exactitud,strHorario,strTipoVisita,strCosto,strNumSerie));
                                                                                                            adapterCantidadBasculas=new AdapterCantidadBasculas(activity,R.layout.item6,listaCantidadBasc,getResources());
                                                                                                            recycler_numero_basc.setAdapter(adapterCantidadBasculas);

                                                                                                            Log.e("t1",strAprobacion);
                                                                                                            Log.e("t2",strMarca);
                                                                                                            Log.e("t3",strModelo);
                                                                                                            Log.e("t4",strAlcance_max);
                                                                                                            Log.e("t5",strAlcance_min);
                                                                                                            Log.e("t6",strCodigo_marca);
                                                                                                            Log.e("t7",strCodigo_modelo);
                                                                                                            Log.e("t8",strAno_modelo);
                                                                                                            Log.e("t9",strEod);
                                                                                                            Log.e("t10",stTipo_instrumento);
                                                                                                            Log.e("t11",strClase_exactitud);
                                                                                                            Log.e("t12",strMarco_pesas);
                                                                                                            Log.e("t13",strPesas_5kg);
                                                                                                            Log.e("t14",stPesas_10kg);                                                                                                    Log.e("t13",strMarco_pesas);
                                                                                                            Log.e("t15",strPesas_20kg);
                                                                                                            Log.e("t16",strPesa_clase_exactitud);
                                                                                                            Log.e("t17",strHorario);
                                                                                                            Log.e("t19",strTipoVisita);
                                                                                                            Log.e("t18",strCosto);
                                                                                                            Log.e("t18",strNumSerie);

                                                                                                            //Log.e("json"+i,jsonObject.getString(nueva_marca));
                                                                                                        } catch (JSONException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }
                                                                                                    }


                                                                                                } catch (JSONException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                            }
                                                                                            else{
                                                                                                Toast.makeText(getApplicationContext(), "El numero de serie es necesario.", Toast.LENGTH_LONG).show();

                                                                                            }


                                                                                        }

                                                                                    }


                                                                                    else{
                                                                                        Toast.makeText(getApplicationContext(), "El costo es necesario.", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                }
                                                                                else{
                                                                                    Toast.makeText(getApplicationContext(), "El horario es necesaio.", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }
                                                                            else{
                                                                                Toast.makeText(getApplicationContext(), "La clase de exactitud es necesaria.", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                        else{
                                                                            Toast.makeText(getApplicationContext(), "Especifique que no aplica.", Toast.LENGTH_LONG).show();
                                                                        }

                                                                    }
                                                                    else{
                                                                        Toast.makeText(getApplicationContext(), "Especifique si no aplic.", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                                else{
                                                                    Toast.makeText(getApplicationContext(), "Especifique si no aplica.", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                            else{
                                                                Toast.makeText(getApplicationContext(), "El marco es necesario.", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                        else{
                                                            Toast.makeText(getApplicationContext(), "La clase de exactitud es necesaria.", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(), "El tipo de instrumento es necesario.", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "El eod es necesario.", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "El año es necesario.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "El codigo del modelo es necesario.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "El codigo de la marca es necesaria.", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Alcance Minimo es necesario.", Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "El alcance Maximo es necesario.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Seleccionar Modelo.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Ingrese Marca.", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Ingrese numero de aprobacion.", Toast.LENGTH_LONG).show();
                }

            }
        });
        regresar_otravez_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                almacen_basculas.setVisibility(View.GONE);
                formulario_principal.setVisibility(View.VISIBLE);
            }
        });
        guardar_marca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nueva_marca = marca_snAprobacion.getText().toString();
                marca_snAprobacion_final.setText(nueva_marca);
                if (!nueva_marca.trim().equals("")) {
                    caja_snAprobacion_marca.setVisibility(View.GONE);
                    caja_snAprobacion_marca_final.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "La marca es necesaria.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_marca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_snAprobacion_marca_final.setVisibility(view.GONE);
                caja_snAprobacion_marca.setVisibility(view.VISIBLE);
            }
        });
        guardar_modelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nueva_modelo = modelo_snAprobacion.getText().toString();
                modelo_snAprobacion_final.setText(nueva_modelo);
                if (!nueva_modelo.trim().equals("")) {
                    caja_snAprobacion_modelo.setVisibility(View.GONE);
                    caja_snAprobacion_modelo_final.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "El modelo es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_modelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_snAprobacion_modelo_final.setVisibility(view.GONE);
                caja_snAprobacion_modelo.setVisibility(view.VISIBLE);
            }
        });
        guardar_alcance_snAprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conexion6=new Conexion(getApplicationContext(),"catalogo",null,1);
                database6=conexion6.getReadableDatabase();
                nueva_alcanceMax = alcanceSnAprobacion_texto.getText().toString();
                alcanceSnAprobacion.setText(nueva_alcanceMax);
                if (!nueva_marca.trim().equals("")) {

                    caja_alcanceSnaprobacion_final.setVisibility(View.VISIBLE);
                    caja_alcanceSnaprobacion.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "El alcance max  es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_alcance_snAprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_alcanceSnaprobacion_final.setVisibility(view.GONE);
                caja_alcanceSnaprobacion.setVisibility(view.VISIBLE);
            }
        });
        guardar_alcanceMin_snAprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nueva_alcanceMin = alcanceMinSnAprobacion_texto.getText().toString();
                alcanceMinSnAprobacion.setText(nueva_alcanceMin);
                if (!nueva_marca.trim().equals("")) {
                    caja_alcanceSMinnaprobacion_final.setVisibility(View.VISIBLE);
                    caja_alcanceMinSnaprobacion.setVisibility(View.GONE);
                    try {
                        String[] parametros6 = {nueva_alcanceMax.trim(),nueva_alcanceMin.trim()};
                        cursor6= database6.rawQuery("SELECT alcance_maximo,alcance_minimo,eod,tipo,clase_exactitud,marco_pesas,pesa_5kg,pesa_10kg,pesa_20kg,pesa_clase_exactitud,horario,alcance_medicion FROM catalogo WHERE alcance_maximo=? AND alcance_minimo=?",parametros6);
                        //Log.e("",""+parametros2);
                        int cuenta3 =cursor6.getCount();
                        Log.e("catalogo",""+cuenta3);
                        while (cursor6.moveToNext()){
                            Log.e("2",cursor6.getString(0));

                            recycler_eod.setText(cursor6.getString(2));
                            Log.e("eod2",cursor6.getString(2));
                            tipoInstrumento.setText(cursor6.getString(3));
                            claseExactitud.setText(cursor6.getString(4));
                            marco_pesas.setText(cursor6.getString(5));
                            pesas_5kg.setText(cursor6.getString(6));
                            pesas_10kg.setText(cursor6.getString(7));
                            pesas_20kg.setText(cursor6.getString(8));
                            pesa_clase_exactitud.setText(cursor6.getString(9));
                            horario.setText(cursor6.getString(10));
                            Log.e("alcance de instrumnento",cursor6.getString(11));
                            eodSnaprobacion=cursor6.getString(2);
                            tipoInstrumentoSnaprobacion=cursor6.getString(3);
                            claseExactitudSnaprobacion=cursor6.getString(4);
                            marco_pesasSnaprobacion=cursor6.getString(5);
                            pesas_5kgSnaprobacion=cursor6.getString(6);
                            pesas_10kgSnaprobacion=cursor6.getString(7);
                            pesas_20kgSnaprobacion=cursor6.getString(8);
                            esa_clase_exactitudSnaprobacion=cursor6.getString(9);
                            horarioSnaprobacion=cursor6.getString(10);
                            nuevo_alcanceMedicion=cursor6.getString(11);

                        }
                        Log.e("eod",cursor6+" -- ");
                    }catch (Exception e){
                        Log.e("catalogo?_amanita","no existe");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "El alcance max  es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_alcanceMin_snAprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_alcanceSMinnaprobacion_final.setVisibility(view.GONE);
                caja_alcanceMinSnaprobacion.setVisibility(view.VISIBLE);
            }
        });
        guardar_Codigomarca_Snaprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nueva_codigoMarcaSnaprobacion = Codigomarca_Snaprobacion_texto.getText().toString();
                CodigomarcaSnaprobacion.setText(nueva_codigoMarcaSnaprobacion);
                if (!nueva_codigoMarcaSnaprobacion.trim().equals("")) {
                    caja_Codigomarca_Snaprobacion_final.setVisibility(View.VISIBLE);
                    caja_Codigomarca_Snaprobacion.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "El alcance max  es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_Codigomarca_Snaprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_Codigomarca_Snaprobacion_final.setVisibility(view.GONE);
                caja_Codigomarca_Snaprobacion.setVisibility(view.VISIBLE);
            }
        });
        guardar_CodigomaModelo_Snaprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nueva_codigoModelo_snAprobacion = CodigomaModelo_Snaprobacion_texto.getText().toString();
                CodigomaModeloSnaprobacion.setText(nueva_codigoModelo_snAprobacion);
                if (!nueva_codigoModelo_snAprobacion.trim().equals("")) {
                    caja_CodigomaModelo_Snaprobacion_final.setVisibility(View.VISIBLE);
                    caja_CodigomaModelo_Snaprobacion.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "El alcance max  es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_CodigomaModelo_Snaprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_CodigomaModelo_Snaprobacion_final.setVisibility(view.GONE);
                caja_CodigomaModelo_Snaprobacion.setVisibility(view.VISIBLE);
            }
        });
        guardar_ano_Snaprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nueva_anoSnapro = ano_Snaprobacion_texto.getText().toString();
                anoSnaprobacion.setText(nueva_anoSnapro);
                if (!nueva_anoSnapro.trim().equals("")) {
                    caja_ano_Snaprobacion_final.setVisibility(View.VISIBLE);
                    caja_ano_Snaprobacion.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "El alcance max  es necesario.", Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_ano_Snaprobacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_ano_Snaprobacion_final.setVisibility(view.GONE);
                caja_ano_Snaprobacion.setVisibility(view.VISIBLE);
            }
        });
        agregar_otra_bascula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulario_bascula.setVisibility(View.VISIBLE);
                autoAprobacion.setText("");
                recycler_marca.setText("");
                caja_auto_aproba.setVisibility(View.VISIBLE);
                caja_aprobacion_final.setVisibility(View.GONE);
                tipoInstrumento.setText("");
                recycler_modelo.setAdapter(adapterModeloBasculas);
                recycler_alcance.setText("");
                alcanceSnAprobacion_texto.setText("");
                caja_recycler_alcance.setVisibility(View.VISIBLE);
                caja_alcanceSnaprobacion_final.setVisibility(View.GONE);
                caja_alcanceSnaprobacion.setVisibility(View.GONE);
                alcanceMinSnAprobacion_texto.setText("");
                caja_recycler_minimo.setVisibility(View.VISIBLE);
                caja_alcanceSnaprobacion_final.setVisibility(View.GONE);
                caja_alcanceSMinnaprobacion_final.setVisibility(View.GONE);
                //recycler_eod.setAdapter(adapterEoD);
                recycler_minimo.setText("");
                codigo_marca.setText("");
                claseExactitud.setText("");
                valorCheckboxPrimera="";
                costo_texto.setText("");
                codigo_modelo.setText("");
                ano_aprobacion.setText("");
                recycler_eod.setText("");
                marco_pesas.setText("");
                pesas_5kg.setText("");
                pesas_10kg.setText("");
                pesas_20kg.setText("");
                horario.setText("");
                tipo_visita.setText("");
                pesa_clase_exactitud.setText("");
                caja_costo_final.setVisibility(View.GONE);
                caja_edit_costo.setVisibility(View.VISIBLE);
                numSerie_texto.setText("");
                caja_numSerie_final.setVisibility(View.GONE);
                caja_edit_numSerie.setVisibility(View.VISIBLE);
                primera_si.setChecked(false);
                primera_no.setChecked(false);
                factura_si.setChecked(false);
                factura_no.setChecked(false);
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng punto1 = new LatLng(19.4234247, -99.1269502);
        LatLng punto2 = new LatLng(19.4240088, -99.1385345);
        LatLng punto3 = new LatLng(19.3543387, -99.0942125);
        LatLng punto4 = new LatLng(19.3498456, -99.0843095);
        LatLng punto5 = new LatLng(19.3506611, -99.0864875);
        LatLng punto6 = new LatLng(19.3411523, -99.103033);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(punto1).title("Mercado morelos"));
        mMap.addMarker(new MarkerOptions().position(punto2).title("Mercado sonora"));
        mMap.addMarker(new MarkerOptions().position(punto3).title("Mercado renovacion"));
        mMap.addMarker(new MarkerOptions().position(punto4).title("Mercado Margarita Maza de juarez"));
        mMap.addMarker(new MarkerOptions().position(punto5).title("Mercado topo"));
        mMap.addMarker(new MarkerOptions().position(punto6).title("Mercado HUEVO"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        miLatLong();


        Location punto_mercado = new Location("Bascula");
        punto_mercado.setLatitude(19.3506611);
        punto_mercado.setLongitude(-99.0864875);
        Location punto_usuario = new Location("Usuario");
        punto_usuario.setLatitude(latitud);
        punto_usuario.setLongitude(longitud);
        Log.e("mercado", "" + punto_mercado);
        Log.e("usuario", "" + punto_usuario);

        float distancias = punto_mercado.distanceTo(punto_usuario);
        float restriccion = 80;
        Log.e("distancia", "" + distancias);
        if (distancias < restriccion) {
            iniciar_verificacion.setVisibility(View.VISIBLE);

        } else {
            Log.e("distancia2", "" + distancias);
            Toast.makeText(getApplicationContext(), "Aun no llegas a tu destino.", Toast.LENGTH_LONG).show();

        }

    }

    private void solicitarPermisoLocation() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISO_LOCATION);
        Log.e("va", "va");
    }

    public void checarPermisos() {
        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {
            //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            Intent irPermisos = new Intent(Mapa.this, ActivarPermisos.class);
            startActivity(irPermisos);
        }
    }

    private void miLatLong(){

        /** SE PIDEN PERMISOS DE LOCACIÓN PARA*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        /** MANAGER DE LOCACIONES DE ANDROID*/
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        actualizarUbicacion(location);


        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Long.parseLong(String.valueOf(tiempo_actualizacion)), 0, locationListener);
    }


    private void actualizarUbicacion(Location location) {
        if (location != null) {
            latitud = location.getLatitude();
            longitud = location.getLongitude();
            altitud=location.getAltitude();
            agregarMarcadorUbicacion(latitud, longitud);
            direccion = darDireccion(this, latitud, longitud);
            UTMPoint p = fromGeodeticToUTM(longitud, latitud);
            LatLng coord = new LatLng(latitud,longitud);
            String[] direccion_fragmentada=direccion.split(",");
            for (int i=0;i<direccion_fragmentada.length;i++){
                calle_str=direccion_fragmentada[0];
                colonia_str=direccion_fragmentada[1];
                delegacion_str=direccion_fragmentada[2];
                cp_str=direccion_fragmentada[3];
                ciudad_str=direccion_fragmentada[4];
                pais_str=direccion_fragmentada[5];
                Log.e("direccion_fragmentada",direccion_fragmentada[i]);
            }
            Log.e("lat",""+latitud);
            Log.e("lon",""+longitud);
            coordenadas =coord;
            direccion_mercado.setText(direccion);
            UTMPoint conver=fromGeodeticToUTM(punto);
            Log.e("con",""+conver);
            UTMPoint conver1=fromGeodeticToUTM(longitud,latitud);
            Log.e("con1",""+conver1);
            UTMPoint conver2=fromGeodeticToUTM(longitud,latitud,WGS84);
            Log.e("con2",""+conver2);

            String strConver= String.valueOf(conver2);
            Log.e("str",""+strConver);

            String[] X_Y_Z=strConver.split(" ");
            Log.e("corte",""+X_Y_Z[3].toString().replace(",","").replace("(",""));
            String x= String.valueOf(conver2.getX());
            String y=String.valueOf(conver2.getY());
            String z=String.valueOf(conver2.getZone());

            latitud_x.setText(X_Y_Z[0].toString().replace(",","").replace("(",""));
            nueva_x=latitud_x.getText().toString();
            longitud_y.setText(X_Y_Z[1].toString().replace(",","").replace("(",""));
            nueva_y=longitud_y.getText().toString();
            GeodeticPoint conver3=fromUTMToGeodetic(x,y,z);
            zona.setText(X_Y_Z[3].toString().replace(",","").replace("(","").replace(")","")+"N");
            nueva_zona=zona.getText().toString();
            Log.e("con3",""+conver3);






            //Toast.makeText(getApplicationContext(),"direccion: "+direccion,Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"lat: "+latitud+"long:"+longitud,Toast.LENGTH_LONG).show();
            Context context = this;
            SharedPreferences preferencias = getSharedPreferences("Usuario", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("latitud", String.valueOf(latitud));
            editor.putString("longitud", String.valueOf(longitud));
            editor.putString("direccion", "" + direccion);
            editor.apply();
            if (latitud!=0){
                //segundoPlano = new SegundoPlano();
                //segundoPlano.execute();
            }
        }
    }
    public String darDireccion(Context ctx, double darLat, double darLong) {
        String fullDireccion = null;
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> direcciones = geocoder.getFromLocation(darLat, darLong, 1);
            if (direcciones.size() > 0) {
                Address direccion = direcciones.get(0);
                fullDireccion = direccion.getAddressLine(0);
                String ciudad = direccion.getLocality();
                String pueblo = direccion.getCountryName();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fullDireccion;
    }
    public Bitmap resizeBitmap(String drawableName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(drawableName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
    private void agregarMarcadorUbicacion(double latitud, double longitud) {

        latLong = new LatLng(latitud, longitud);
        if (marker != null) {
            marker.remove();
            marker = mMap.addMarker(new MarkerOptions()
                    .position(latLong)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("bascula", 70, 70))));
            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(latLong, 18);
            mMap.moveCamera(miUbicacion);
        }
        else{
            marker = mMap.addMarker(new MarkerOptions()
                    .position(latLong)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("bascula", 70, 70))));
            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(latLong, 18);
            mMap.animateCamera(miUbicacion);
        }

        marker.setZIndex(0);
    }
    LocationListener locationListener = new LocationListener() {
        //Cuando la ubicación cambia
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
            Location locationA = new Location("Primera");
            latUpdate = location.getLatitude();
            longUpdate = location.getLongitude();
            locationA.setLatitude(latUpdate);
            locationA.setLongitude(longUpdate);
            //Location locationB = new Location("point B");
            //String latitudX = sharedPreferences.getString("latitud", "no");
            //String longitudX = sharedPreferences.getString("longitud", "no");
            /*if (!latitudX.equals("no") && !longitudX.equals("no")) {
                locationB.setLatitude(Double.parseDouble(latitudX));
                locationB.setLongitude(Double.parseDouble(longitudX));
                float distance = locationA.distanceTo(locationB);
                if (distance >= 3)//era 10 antes
                {
                    mMap.clear();
                    miLatLong();
                }
                //strLat = datosViaje.getString("lat"," ");
                //strLong = datosViaje.getString("lng"," ");
                //strEmpresa = datosViaje.getString("empresa"," ");
                //strEstado = datosViaje.getString("estado"," ");
                if (strEstado.equals("destino")){
                    Location locationC = new Location("point B");
                    locationC.setLatitude(Double.parseDouble(strLat));
                    locationC.setLongitude(Double.parseDouble(strLong));
                    float distanciaDestino = locationC.distanceTo(locationA);
                    Log.e("distancia",""+distanciaDestino);
                    if (distanciaDestino < 70)//distancia en metros a punto de partida
                    {
                        //Log.e("distancia","es menor a 1");
                        //if ()
                        capaDestino.setVisibility(View.GONE);
                        capaLlegada.setVisibility(View.VISIBLE);
                    }
                }
            }*/
        }
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
        }
    };
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISO_LOCATION : {
                Log.e("confirma","confirma");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Intent reiniciar=new Intent(Mapa.this,ActivarPermisos.class);
                    startActivity(reiniciar);
                } else {
                    Intent valcuacion=new Intent(Mapa.this,Mapa.class);
                    startActivity(valcuacion);

                    Log.e("AQUIMAMO","AQUIMAMO");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void consultarFormaMedicamento(){

        conexion1=new Conexion(getApplicationContext(),"catalogo",null,1);
        database=conexion1.getReadableDatabase();
        conexion2=new Conexion(getApplicationContext(),"basculas",null,1);
        database2=conexion2.getReadableDatabase();



        try {
            cursor1= database.rawQuery("SELECT alcance_maximo,eod,tipo,alcance_minimo,alcance_medicion,clase_exactitud,marco_pesas,pesa_5kg,pesa_10kg,pesa_20kg,pesa_clase_exactitud,horario FROM catalogo  ",null);
            // Log.e("tipos",""+cursor1);
            int cuenta =cursor1.getCount();
            Log.e("catalogo",""+cuenta);
            while (cursor1.moveToNext()){
                Log.e("*",cursor1.getString(0));
                Log.e("horario",cursor1.getString(1));
            }
            Log.e("horario",cuenta+" -- ");
        }catch (Exception e){
            Log.e("catalogo","no existe");
        }
        try {
            cursor2= database2.rawQuery("SELECT DISTINCT numero_aprobacion FROM basculas  ",null);
            // Log.e("tipos",""+cursor1);
            int cuenta =cursor2.getCount();
            Log.e("basculas",""+cuenta);
            while (cursor2.moveToNext()){
                Log.e("marca",cursor2.getString(0));
                APROBACION.add(cursor2.getString(0));
                //Log.e("lista_basculas",""+BASCULAS);
            }
            Log.e("lista_basculas",""+ APROBACION);
        }catch (Exception e){
            Log.e("basculas","no existe");
        }
    }
    public void setListaGiro()
    {
        listaGiro.clear();
        String coy[] = {"", "Abarrotes","Cafetería",
                "Carnicería", "Casas de empeño","Chocolatería","Compra-venta de chatarra",
                "Compra-venta de papel y cartón", "Confitería","Cremería","Desperdicios Industriales",
                "Dulcería", "Ferretería","Huevo","Joyería",
                "Lavandería", "Materiales de Construcción", "Mercado sobre ruedas",
                "Mini súper", "Miscelánea","Molino","Panadería",
                "Pescados y mariscos", "Pollería","Productos del Campo","Recaudería",
                "Restaurant", "Rosticería","Semillas y chiles secos","Supermercados",
                "Taquería", "Tiendas Departamentales","Tlapalería", "Tortillería","Otros","Contenedores Marítimos",
                "Agropecuarias y/o Ganaderas", "Comerciales","De Servicios","Industriales",
                "Básculas Públicas", "Industria Automotriz"};
        for (int i=0; i<coy.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaGiro.add(sched);
        }
    }



    public void setListaModelo()
    {
//        for(int i=0;i<listaModelo.size();i++){
//            Log.e("objeto");
//        }
        adapterModeloBasculas = new AdapterModeloBasculas(activity, R.layout.item2, listaModelo, getResources());
        recycler_modelo.setAdapter(adapterModeloBasculas);
    }




    public void setListaCantidadBasc()
    {

    }

    @Override
    public void onBackPressed() {

        formulario_bascula.setVisibility(View.GONE);

    }
    private void limpiarAcentos(String cadena){
        String limpia=cadena.replace("á","a");
        limpia=limpia.replace("é","e");
        limpia=limpia.replace("í","i");
        limpia=limpia.replace("ó","o");
        limpia=limpia.replace("ú","u");

        Log.e("busqueda",limpia);
        autoAprobacion.setText(limpia);
    }

    public void definirAlcance(String modelo){
        conexion1=new Conexion(getApplicationContext(),"catalogo",null,1);
        database=conexion1.getReadableDatabase();
        conexion4=new Conexion(getApplicationContext(),"basculas",null,1);
        database4=conexion4.getReadableDatabase();
        aprobacion_basc.setText(nueva_aprobacion);

        modelo.trim();
        Log.e("MODELO_MAPA",""+modelo);
        try {
            String[] parametros = {modelo.trim()};
            Log.e("parametros",""+modelo);
            cursor4= database4.rawQuery("SELECT alcance_maximo,alcance_minimo,modelo FROM basculas WHERE modelo=?",parametros);
            Log.e("alcance",""+cursor4);
            int cuenta =cursor4.getCount();

            while (cursor4.moveToNext()){
                Log.e("alcance",cursor4.getString(0));
                recycler_alcance.setText(cursor4.getString(0));
                recycler_minimo.setText(cursor4.getString(1));
                nuevo_alcanceMax_aprobado=cursor4.getString(0);
                nuevo_alcanceMin_aprobado=cursor4.getString(1);

                if(nuevo_alcanceMax_aprobado.equals(" ")&&nuevo_alcanceMin_aprobado.equals(" ")){
                    Log.e("quepasa",""+nueva_alcanceMax);
                    caja_recycler_alcance.setVisibility(View.GONE);
                    caja_alcanceSnaprobacion.setVisibility(View.VISIBLE);
                    caja_recycler_minimo.setVisibility(View.GONE);
                    caja_alcanceMinSnaprobacion.setVisibility(View.VISIBLE);
                }else{
                    try {
                        String[] parametros2 = {cursor4.getString(0),cursor4.getString(1)};
                        cursor1= database.rawQuery("SELECT alcance_maximo,alcance_minimo,eod,tipo,clase_exactitud,marco_pesas,pesa_5kg,pesa_10kg,pesa_20kg,pesa_clase_exactitud,horario,alcance_medicion FROM catalogo WHERE alcance_maximo=? AND alcance_minimo=?",parametros2);
                        //Log.e("tipos",""+parametros2);
                        int cuenta2 =cursor1.getCount();
                        Log.e("catalogo",""+cuenta2);
                        while (cursor1.moveToNext()){
                            Log.e("1",cursor1.getString(0));
                            Log.e("eod",cursor1.getString(2));
                            recycler_eod.setText(cursor1.getString(2));
                            nuevo_eod=cursor1.getString(2);
                            Log.e("putoeod",""+nuevo_eod);
                            tipoInstrumento.setText(cursor1.getString(3));
                            claseExactitud.setText(cursor1.getString(4));
                            marco_pesas.setText(cursor1.getString(5));
                            pesas_5kg.setText(cursor1.getString(6));
                            pesas_10kg.setText(cursor1.getString(7));
                            pesas_20kg.setText(cursor1.getString(8));
                            pesa_clase_exactitud.setText(cursor1.getString(9));
                            horario.setText(cursor1.getString(10));
                            nuevo_tipoInstrumento=cursor1.getString(3);
                            nueva_claseExactitud=cursor1.getString(4);
                            nuevo_marco_pesas=cursor1.getString(5);
                            nuevo_pesas_5kg=cursor1.getString(6);
                            nuevo_pesas_10kg=cursor1.getString(7);
                            nuevo_pesas_20kg=cursor1.getString(8);
                            nuevo_pesa_clase_exactitud= cursor1.getString(9);
                            nuevo_horario=cursor1.getString(10);
                            nuevo_alcanceMedicion=cursor1.getString(11);

                        }
                        Log.e("eod",cuenta2+" -- ");
                    }catch (Exception e){
                        Log.e("catalogo","no existe");
                    }

                }


            }


            Log.e("alcance_cuenta",""+cuenta);
            //listaAlcance.clear();


            Log.e("LISTA_aLCANCE",""+cursor4);
        }catch (Exception e){
            Log.e("ALCANCE_MAX","no existe");
        }

    }
    private void quitar_foco()
    {
        primera_no.setChecked(false);
        primera_si.setChecked(false);

    }

    private void quitar_foco2()
    {

        factura_no.setChecked(false);
        factura_si.setChecked(false);
    }
    private void quitar_foco3()
    {
        relacion_comercial_si.setChecked(false);
        relacion_comercial_no.setChecked(false);

    }

    private void quitar_foco4()
    {

        amenaza_integridad_si.setChecked(false);
        amenaza_integridad_no.setChecked(false);
    }
    private void quitar_foco5()
    {

        conflicto_intereses_si.setChecked(false);
        conflicto_intereses_no.setChecked(false);
    }

    private class AsincronaEnviarBasculas extends AsyncTask<Void, Integer,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            enviarBasculas();
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
    public void enviarBasculas()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"enviar_basculas.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta4:",response + "sal");
                        if(response.equals("success")){
//                            Intent intent = new Intent(Registro.this,Login.class);
//                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("respuesta4Error:",error + "error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();


                map.put("id",id_usuer);
                map.put("id_sesion",id_SesionUsuer);
                map.put("fecha",nueva_fecha);
                map.put("nombre_usuario",nuevo_nombre);
                map.put("rfc",nuevo_rfc);
                map.put("calle",calle_str);
                map.put("colonia",colonia_str);
                map.put("delegacion",delegacion_str);
                map.put("codigo_postal",cp_str);
                map.put("ciudad",ciudad_str);
                map.put("pais",pais_str);
                map.put("giro",seleccion_giro);
                map.put("mercado",nuevo_mercado);
                map.put("telefono",nuevo_tel);
                map.put("zona",nueva_zona);
                map.put("coordenadax",nueva_x);
                map.put("coordenaday",nueva_y);
                map.put("basculas",str_final);
                map.put("numero_basculas", String.valueOf(cuenta_basculas));
                map.put("primera_vez", String.valueOf(valorCheckboxPrimera));
                map.put("factura", String.valueOf(valorCheckboxFactura));
                map.put("intereses_comercial", String.valueOf(valorCheckboxComercial));
                map.put("intereses_integridad", String.valueOf(valorCheckboxIntegridad));
                map.put("intereses_personales", String.valueOf(valorCheckboxIntereses));

                return map;
            }
        };
        requestQueue.add(request);
    }

}