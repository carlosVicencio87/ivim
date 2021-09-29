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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int PERMISO_LOCATION=1;
    private LocationManager locationManager;
    private LatLng coord,coordenadas,latLong;
    private Marker marker;
    private ConstraintLayout mapaid,caja_fecha,cons_check;
    private LinearLayout caja_punto_partida,caja_edit_nombre,caja_nombre_final,
            caja_direccion,caja_giro,caja_mercado,caja_edit_tel,caja_tel_final,
            caja_recycler_marca,caja_recycler_modelo,caja_siguiente_tab,caja_x,caja_longitud,caja_zona,
            caja_instrumento,caja_edit_serie,caja_serie_final,caja_recycler_alcance;
    private Fragment map;
    private int check=0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Mapa activity;
    private double latitud,longitud,altitud,latUpdate,longUpdate;
    private String direccion,nuevo_nombre,seleccion_giro,nuevo_tel,nueva_serie;
    private TextView puntoPartida,nombre,direccion_mercado,telefono,latitud_x,longitud_y,zona,numero_serie;
    private EditText nombre_texto,fecha,tel_texto,serie_texto;
    private ImageView iniciar_verificacion,guardar_nombre,cambiar_nombre,guardar_tel,
            cambiar_telefono,siguiente_tab,guardar_serie,cambiar_serie;
    private CheckBox inicial,anual,primerSemestre,segundoSemestre,extraordinaria;
    private RecyclerView recycler_marca,recycler_modelo,recycler_alcance;
    private Boolean tel10;
    private SharedPreferences datosUsuario;
    private ScrollView formulario_principal,formulario_bascula;
    private Spinner giros,mercado,tipoInstrumento;
    public ArrayList<SpinnerModel> listaGiro= new ArrayList<>();
    private AdapterGiro adapterGiro;
    private AdapterMercado adapterMercado;

    public ArrayList<SpinnerModel> listaMercado= new ArrayList<>();
    private RecyclerView recyclerMarca,recyclerModelo,recyclerAlcance;
    private AdapterTipoInstrumento adapterTipoInstrumento;
    public ArrayList<SpinnerModel> listaInstrumen= new ArrayList<>();
    private AdapterMarcaBasculas adapterMarcaBasculas;
    private AdapterModeloBasculas adapterModeloBasculas;
    private AdapterAlcanceMax adapterAlcanceMax;
    private ArrayList<AlcanceRecycler>listaAlcance;
    private ArrayList<ModeloRecycler>listaModelo;
    private ArrayList<MarcaRecycler> listaMarca;
    private Context context;
    public final static int WGS84 = 0;
    public final static int HAYFORD = 1;
    private final static double[] ELLIPSOID_A = {6378137.000, 6378388.000};
    private final static double[] ELLIPSOID_B = {6356752.3142449996, 6356911.946130};
    public UTMPoint p;
    public CoordinateConverter convertidor;
    public GeodeticPoint punto=new GeodeticPoint(latitud,longitud,altitud);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapaid=findViewById(R.id.mapaid);
        puntoPartida=findViewById(R.id.puntoPartida);
        iniciar_verificacion=findViewById(R.id.iniciar_verificacion);
        nombre=findViewById(R.id.nombre);
        nombre_texto=findViewById(R.id.nombre_texto);
        formulario_principal=findViewById(R.id.formulario_principal);
        caja_punto_partida=findViewById(R.id.caja_punto_partida);
        caja_edit_nombre=findViewById(R.id.caja_edit_nombre);
        caja_nombre_final=findViewById(R.id.caja_nombre_final);
        guardar_nombre=findViewById(R.id.guardar_nombre);
        cambiar_nombre=findViewById(R.id.cambiar_nombre);
        caja_fecha=findViewById(R.id.caja_fecha);
        fecha=findViewById(R.id.fecha);
        caja_direccion=findViewById(R.id.caja_direccion);
        direccion_mercado=findViewById(R.id.direccion_mercado);
        caja_giro=findViewById(R.id.caja_giro);
        giros=findViewById(R.id.giros);
        caja_mercado=findViewById(R.id.caja_mercado);
        mercado=findViewById(R.id.mercado);
        caja_edit_tel=findViewById(R.id.caja_edit_tel);
        tel_texto=findViewById(R.id.tel_texto);
        guardar_tel=findViewById(R.id.guardar_tel);
        caja_tel_final=findViewById(R.id.caja_tel_final);
        cambiar_telefono=findViewById(R.id.cambiar_telefono);
        telefono=findViewById(R.id.telefono);
        cons_check=findViewById(R.id.cons_check);
        inicial=findViewById(R.id.inicial);
        anual=findViewById(R.id.anual);
        primerSemestre=findViewById(R.id.primerSemestre);
        segundoSemestre=findViewById(R.id.segundoSemestre);
        extraordinaria=findViewById(R.id.extraordinaria);
        activity = this;
        setListaGiro();
        setListaMercado();
        setListaInstrumen();

        listaMarca = new ArrayList<>();
        setListaMarca();
        listaModelo=new ArrayList<>();
        setListaModelo();

        listaAlcance=new ArrayList<>();
        setListaAlcance();
        caja_siguiente_tab=findViewById(R.id.caja_siguiente_tab);
        siguiente_tab=findViewById(R.id.siguiente_tab);
        formulario_bascula=findViewById(R.id.formulario_bascula);
        context = this;
        recyclerMarca =(RecyclerView) findViewById(R.id.recycler_marca);
        recyclerMarca.setLayoutManager(new LinearLayoutManager(context));
        recyclerModelo=findViewById(R.id.recycler_modelo);
        recyclerModelo.setLayoutManager(new LinearLayoutManager(context));
        recyclerAlcance=findViewById(R.id.recycler_alcance);
        recyclerAlcance.setLayoutManager(new LinearLayoutManager(context));



        caja_x=findViewById(R.id.caja_x);
        caja_longitud=findViewById(R.id.caja_longitud);
        latitud_x=findViewById(R.id.latitud_x);
        longitud_y=findViewById(R.id.longitud_y);
        caja_zona=findViewById(R.id.caja_zona);
        zona=findViewById(R.id.zona);
        caja_instrumento=findViewById(R.id.caja_instrumento);
        tipoInstrumento=findViewById(R.id.tipoInstrumento);
        caja_recycler_marca =findViewById(R.id.caja_recycler_marca);
        caja_recycler_modelo =findViewById(R.id.caja_recycler_modelo);
        recycler_marca =findViewById(R.id.recycler_marca);
        recycler_modelo =findViewById(R.id.recycler_modelo);
        caja_edit_serie =findViewById(R.id.caja_edit_serie);
        serie_texto =findViewById(R.id.serie_texto);
        guardar_serie =findViewById(R.id.guardar_serie);
        caja_serie_final =findViewById(R.id.caja_serie_final);
        numero_serie =findViewById(R.id.numero_serie);
        cambiar_serie =findViewById(R.id.cambiar_serie);
        caja_recycler_alcance =findViewById(R.id.caja_recycler_alcance);
        recycler_alcance =findViewById(R.id.recycler_alcance);



        final int permisoLocacion = ContextCompat.checkSelfPermission(Mapa.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permisoLocacion!= PackageManager.PERMISSION_GRANTED) {
            solicitarPermisoLocation();

            Log.e("paso","paso");
        }

        convertidor=new CoordinateConverter();
        UTMPoint p = fromGeodeticToUTM(longitud, latitud);



        iniciar_verificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            formulario_principal.setVisibility(View.VISIBLE);
            mapaid.setVisibility(View.GONE);
            caja_punto_partida.setVisibility(View.GONE);

            }
        });

        guardar_nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 nuevo_nombre=nombre_texto.getText().toString();
                 nombre.setText(nuevo_nombre);
                 if(!nuevo_nombre.trim().equals(""))
                 {
                     caja_edit_nombre.setVisibility(View.GONE);
                     caja_nombre_final.setVisibility(View.VISIBLE);
                 }
                 else{
                     Toast.makeText(getApplicationContext(),"El nombre es necesario.",Toast.LENGTH_LONG).show();
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
        guardar_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cuenta = tel_texto.getText().toString().trim().length();
                if (cuenta == 10) {
                    tel10 = true;
                } else {
                    tel10 = false;
                }
                nuevo_tel=tel_texto.getText().toString();
                telefono.setText(nuevo_tel);
                if(!nuevo_tel.trim().equals(""))
                {
                    if(tel10==true){


                        caja_edit_tel.setVisibility(View.GONE);
                        caja_tel_final.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"El telefono debe ser de 10 digitos.",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"El telefono es necesario.",Toast.LENGTH_LONG).show();
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


        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()).format(new Date());
        Log.e("fecha",""+date);
        fecha.setText(date);



        adapterGiro = new AdapterGiro(activity,R.layout.lista_giro,listaGiro,getResources());
        giros.setAdapter(adapterGiro);

        adapterMercado = new AdapterMercado(activity,R.layout.lista_mercado,listaMercado,getResources());
        mercado.setAdapter(adapterMercado);

        adapterTipoInstrumento = new AdapterTipoInstrumento(activity,R.layout.lista_tipo_instrumento,listaInstrumen,getResources());
        tipoInstrumento.setAdapter(adapterTipoInstrumento);

        adapterMarcaBasculas = new AdapterMarcaBasculas(activity,R.layout.item, listaMarca,getResources());
        recycler_marca.setAdapter(adapterMarcaBasculas);

        adapterModeloBasculas = new AdapterModeloBasculas(activity,R.layout.item2, listaModelo,getResources());
        recycler_modelo.setAdapter(adapterModeloBasculas);

        adapterAlcanceMax = new AdapterAlcanceMax(activity,R.layout.item3, listaAlcance,getResources());
        recycler_alcance.setAdapter(adapterAlcanceMax);

        guardar_serie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nueva_serie=serie_texto.getText().toString();
                numero_serie.setText(nueva_serie);
                if(!nueva_serie.trim().equals(""))
                {
                    caja_edit_serie.setVisibility(View.GONE);
                    caja_serie_final.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(getApplicationContext(),"El numero de serie es necesario.",Toast.LENGTH_LONG).show();
                }
            }
        });
        cambiar_serie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caja_serie_final.setVisibility(view.GONE);
                caja_edit_serie.setVisibility(view.VISIBLE);
            }
        });


        giros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView listas_giro=findViewById(R.id.listaGiro);
                if (listas_giro==null)
                {
                    listas_giro=(TextView) view.findViewById(R.id.listaGiro);
                }
                else
                {
                    listas_giro=(TextView) view.findViewById(R.id.listaGiro);
                }
                seleccion_giro =listas_giro.getText().toString();
                Log.e("tipomat",""+seleccion_giro);


                Log.e("tipo",""+seleccion_giro);
                Log.e("tipo",""+position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        inicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco();
                inicial.setChecked(true);
            }
        });
        anual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco();
                anual.setChecked(true);
            }
        });
        primerSemestre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco();
                primerSemestre.setChecked(true);
            }
        });
        segundoSemestre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco();
                segundoSemestre.setChecked(true);
            }
        });
        extraordinaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitar_foco();
                extraordinaria.setChecked(true);
            }
        });
        siguiente_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                formulario_principal.setVisibility(view.GONE);
                formulario_bascula.setVisibility(view.VISIBLE);
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
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(punto1).title("Mercado morelos"));
        mMap.addMarker(new MarkerOptions().position(punto2).title("Mercado sonora"));
        mMap.addMarker(new MarkerOptions().position(punto3).title("Mercado renovacion"));
        mMap.addMarker(new MarkerOptions().position(punto4).title("Mercado Margarita Maza de juarez"));
        mMap.addMarker(new MarkerOptions().position(punto5).title("Mercado topo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        miLatLong().getLatitude();
        miLatLong().getLongitude();



       Location punto_mercado= new Location("Bascula");
        punto_mercado.setLatitude(19.3506611);
        punto_mercado.setLongitude(-99.0864875);
       Location punto_usuario= new Location("Usuario");
       punto_usuario.setLatitude(miLatLong().getLatitude());
       punto_usuario.setLongitude(miLatLong().getLongitude());
       Log.e("mercado",""+punto_mercado);
       Log.e("usuario",""+punto_usuario);

       float distancias=punto_mercado.distanceTo(punto_usuario);
       float restriccion=5;
       Log.e("distancia",""+distancias);
       if(distancias<restriccion){
           iniciar_verificacion.setVisibility(View.VISIBLE);
       }
       else {
           Log.e("distancia2",""+distancias);
           Toast.makeText(getApplicationContext(), "Aun no llegas a tu destino.", Toast.LENGTH_LONG).show();
       }

    }
    private void solicitarPermisoLocation() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISO_LOCATION);
        Log.e("va","va");
    }

    public void checarPermisos() {
        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {
            //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            Intent irPermisos = new Intent(Mapa.this, ActivarPermisos.class);
            startActivity(irPermisos);
        }
    }
    private Location miLatLong() {


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 5, locationListener);
        return location;
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
            longitud_y.setText(X_Y_Z[1].toString().replace(",","").replace("(",""));
             GeodeticPoint conver3=fromUTMToGeodetic(x,y,z);
            zona.setText(X_Y_Z[3].toString().replace(",","").replace("(","").replace(")",""));

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
    public void setListaGiro()
    {
        listaGiro.clear();
        String coy[] = {"", "Vendedor de croquetas","M.D.C y columnas de concreto",
                "M.D.C y columnas de acero", "M.D.C y columnas mixtas"};
        for (int i=0; i<coy.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaGiro.add(sched);
        }
    }
    public void setListaMercado()
    {
        listaMercado.clear();
        String coy[] = {"", "Mercado topo","M.D.C y columnas de concreto",
                "M.D.C y columnas de acero", "M.D.C y columnas mixtas"};
        for (int i=0; i<coy.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaMercado.add(sched);
        }
    }
    public void setListaInstrumen()
    {
        listaInstrumen.clear();
        String coy[] = {"", "M=Mecanica","E=Electronica",
                "EM=Electromecanica"};
        for (int i=0; i<coy.length;i++)
        {
            final SpinnerModel sched = new SpinnerModel();
            sched.ponerNombre(coy[i]);
            //sched.ponerImagen("spinner"+i);
            sched.ponerImagen("spi_"+i);
            listaInstrumen.add(sched);
        }
    }
    private void quitar_foco()
    {
        inicial.setChecked(false);
        primerSemestre.setChecked(false);
        segundoSemestre.setChecked(false);
        anual.setChecked(false);
        extraordinaria.setChecked(false);

    }
    public void setListaMarca()
    {
        listaMarca.clear();
        String coy[] = {"Microlife","Braunker",
                "Ohaus","Transcell","Sartorius","Adam equipemet","Sartorius",};
        for (int i=0; i<coy.length;i++)
        {

            listaMarca.add(new MarcaRecycler(coy[i]));
        }
    }

    public void setListaModelo()
    {
        listaModelo.clear();
        String coy[] = {"WS100","WS80",
                "CS500","CS2000","CS200","TR30RS","ESW-5M",};
        for (int i=0; i<coy.length;i++)
        {

            listaModelo.add(new ModeloRecycler(coy[i]));
        }
    }
    public void setListaAlcance()
    {
        listaAlcance.clear();
        String coy[] = {"8/10","10/20",
                "5/10","40/20","50/100","100/200","150/1000",};
        for (int i=0; i<coy.length;i++)
        {

            listaAlcance.add(new AlcanceRecycler(coy[i]));
        }
    }

}