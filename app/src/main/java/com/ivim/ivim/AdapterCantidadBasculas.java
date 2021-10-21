package com.ivim.ivim;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterCantidadBasculas extends RecyclerView.Adapter<AdapterCantidadBasculas.ViewHolderRecycler>{
    private ArrayList<CantidadBasculasRecycler> ranking6recycler;
    ViewHolderRecycler viewholderCantidadBasculaso,anterior;
    private  RecyclerView recyclerView;
    private Context context;
    private String cantidad_bascula,cantidad_tipo_instrumento,cantidad_modelo,cantidad_serie,cantidad_AlcanceMax,cantidad_Eod,
            cantidad_AlcanceMin,cantidad_exactitud,cantidad_checkbox,cantidad_costo;
    private SharedPreferences datosCantidadBascula;
    private SharedPreferences.Editor editor;

    public AdapterCantidadBasculas(Mapa activity, int item, ArrayList<CantidadBasculasRecycler> ranking6recycler, Resources resources)
    {
        this.ranking6recycler=ranking6recycler;
    }
    @Override
    public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item6,parent,false);
        context=parent.getContext();
        datosCantidadBascula= context.getSharedPreferences("modelos",context.MODE_PRIVATE);
        editor=datosCantidadBascula.edit();
        return new ViewHolderRecycler(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterCantidadBasculas.ViewHolderRecycler holder, int position) {
        viewholderCantidadBasculaso =holder;

        cantidad_bascula = ranking6recycler.get(position).getCantidad_bascula();
        holder.cant_bascula.setText(cantidad_bascula);
        cantidad_tipo_instrumento=ranking6recycler.get(position).getCantidad_bascula();
        holder.cant_tipo_instrumento.setText(cantidad_tipo_instrumento);
        cantidad_modelo=ranking6recycler.get(position).getCantidad_modelo();
        holder.cant__modelo.setText(cantidad_modelo);
        cantidad_serie=ranking6recycler.get(position).getCantidad_serie();
        holder.cant_serie.setText(cantidad_serie);
        cantidad_AlcanceMax=ranking6recycler.get(position).getCantidad_AlcanceMax();
        holder.cant_AlcanceMax.setText(cantidad_AlcanceMax);
        cantidad_Eod=ranking6recycler.get(position).getCantidad_Eod();
        holder.cant_Eod.setText(cantidad_Eod);
        cantidad_AlcanceMin=ranking6recycler.get(position).getCantidad_AlcanceMin();
        holder.cant_AlcanceMin.setText(cantidad_AlcanceMin);
        cantidad_exactitud=ranking6recycler.get(position).getCantidad_exactitud();
        holder.cant_exactitud.setText(cantidad_exactitud);
        cantidad_checkbox=ranking6recycler.get(position).getCantidad_checkbox();
        holder.cant_checkbox.setText(cantidad_checkbox);
        cantidad_costo=ranking6recycler.get(position).getCantidad_costo();
        holder.cant_costo.setText(cantidad_costo);

        final String modelo  =  holder.cant_bascula.getText().toString();
        holder.marco5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context,modelo,Toast.LENGTH_SHORT).show();
                editor.putString("modelo",modelo);
                editor.apply();
                Log.e("cambio",datosCantidadBascula.getString("modelo","no"));

                if (anterior!=null)
                {
                    anterior.marco5.setBackgroundResource(R.color.blanco);
                    anterior.cant_bascula.setTextColor(context.getResources().getColor(R.color.blanco));
                }
                holder.marco5.setBackgroundResource(R.color.negro);
                holder.cant_bascula.setTextColor(context.getResources().getColor(R.color.blanco));



                anterior=holder;

            }
        });
    }

    @Override
    public int getItemCount(){
        return ranking6recycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView cant_bascula,cant_tipo_instrumento,cant__modelo,cant_serie,cant_AlcanceMax,cant_Eod,cant_AlcanceMin,cant_exactitud,cant_checkbox,cant_costo;
        LinearLayout marco5;


        public ViewHolderRecycler(View itemView) {
            super(itemView);
            cant_bascula =(TextView)itemView.findViewById(R.id.cantidad_bascula);
            cant_tipo_instrumento =(TextView)itemView.findViewById(R.id.cantidad_tipo_instrumento);
            cant__modelo =(TextView)itemView.findViewById(R.id.cantidad_modelo);
            cant_serie =(TextView)itemView.findViewById(R.id.cantidad_serie);
            cant_AlcanceMax =(TextView)itemView.findViewById(R.id.cantidad_AlcanceMax);
            cant_Eod =(TextView)itemView.findViewById(R.id.cantidad_Eod);
            cant_AlcanceMin =(TextView)itemView.findViewById(R.id.cantidad_AlcanceMin);
            cant_exactitud =(TextView)itemView.findViewById(R.id.cantidad_exactitud);
            cant_checkbox =(TextView)itemView.findViewById(R.id.cantidad_checkbox);
            cant_costo =(TextView)itemView.findViewById(R.id.cantidad_costo);
            marco5=(LinearLayout)itemView.findViewById(R.id.marco5);




        }
    }
}