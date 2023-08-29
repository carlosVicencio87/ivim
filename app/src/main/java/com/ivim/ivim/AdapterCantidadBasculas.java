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
    private String cantidad_numero_aprobacion,cantidad_marca,cantidad_modelo,cantidad_AlcanceMax,cantidad_AlcanceMin,cantidad_CodigoMarca,
            cantidad_CodigoModelo,cantidad_anoAprobacion,cantidad_eod,cantidad_TipoInstrumento,cantidad_claseExactitud,cantidad_marcoPesas,
            cantidad_pesas5kg,cantidad_pesas10kg,cantidad_pesas20kg,cantidad_pesaClase_exactitud,cantidad_horario,cantidad_costo,cantidad_TipoVisita,cantidad_numeroSerie,numero_folio;
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

        cantidad_numero_aprobacion = ranking6recycler.get(position).getCantidad_numero_aprobacion();
        holder.cant_numero_aprobacion.setText(cantidad_numero_aprobacion);
        cantidad_marca=ranking6recycler.get(position).getCantidad_marca();
        holder.cant_marca.setText(cantidad_marca);

        cantidad_modelo=ranking6recycler.get(position).getCantidad_modelo();
        holder.cant_modelo.setText(cantidad_modelo);
        cantidad_AlcanceMax=ranking6recycler.get(position).getCantidad_AlcanceMax();
        holder.cant_AlcanceMax.setText(cantidad_AlcanceMax);
        cantidad_AlcanceMin=ranking6recycler.get(position).getCantidad_AlcanceMin();
        holder.cant_AlcanceMin.setText(cantidad_AlcanceMin);
        cantidad_CodigoMarca=ranking6recycler.get(position).getCantidad_CodigoMarca();
        holder.cant_CodigoMarca.setText(cantidad_CodigoMarca);
        cantidad_CodigoModelo=ranking6recycler.get(position).getCantidad_CodigoModelo();
        holder.cant_CodigoModelo.setText(cantidad_CodigoModelo);

        cantidad_anoAprobacion=ranking6recycler.get(position).getCantidad_anoAprobacion();
        holder.cant_anoAprobacion.setText(cantidad_anoAprobacion);

        cantidad_eod=ranking6recycler.get(position).getCantidad_eod();
        holder.cant_eod.setText(cantidad_eod);

        cantidad_TipoInstrumento=ranking6recycler.get(position).getCantidad_TipoInstrumento();
        holder.cant_TipoInstrumento.setText(cantidad_TipoInstrumento);

        cantidad_claseExactitud=ranking6recycler.get(position).getCantidad_claseExactitud();
        holder.cant_claseExactitud.setText(cantidad_claseExactitud);

        cantidad_marcoPesas=ranking6recycler.get(position).getCantidad_marcoPesas();
        holder.cant_marcoPesas.setText(cantidad_marcoPesas);

        cantidad_pesas5kg=ranking6recycler.get(position).getCantidad_pesas5kg();
        holder.cant_pesas5kg.setText(cantidad_pesas5kg);

        cantidad_pesas10kg=ranking6recycler.get(position).getCantidad_pesas10kg();
        holder.cant_pesas10kg.setText(cantidad_pesas10kg);


        cantidad_pesas20kg=ranking6recycler.get(position).getCantidad_pesas20kg();
        holder.cant_pesas20kg.setText(cantidad_pesas20kg);

        cantidad_pesaClase_exactitud=ranking6recycler.get(position).getCantidad_pesaClase_exactitud();
        holder.cant_pesaClase_exactitud.setText(cantidad_pesaClase_exactitud);

        cantidad_horario=ranking6recycler.get(position).getCantidad_horario();
        holder.cant_horario.setText(cantidad_horario);

        cantidad_TipoVisita=ranking6recycler.get(position).getCantidad_TipoVisita();
        holder.cant_TipoVisita.setText(cantidad_TipoVisita);

        cantidad_costo=ranking6recycler.get(position).getCantidad_costo();
        holder.cant_costo.setText(cantidad_costo);

        cantidad_numeroSerie=ranking6recycler.get(position).getCantidad_numeroSerie();
        holder.cant_numeroSerie.setText(cantidad_numeroSerie);
        numero_folio=ranking6recycler.get(position).getNumero_folio();
        holder.cantidad_numero_folio.setText(numero_folio);

        final String modelo  =  holder.cant_modelo.getText().toString();
        ((Mapa)context).definirAlcance(modelo);

        holder.marco5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int posiTion2 = holder.getAdapterPosition();
                cantidad_numero_aprobacion=ranking6recycler.get(posiTion2).getCantidad_marca();
                cantidad_marca=ranking6recycler.get(posiTion2).getCantidad_marca();
                cantidad_modelo=ranking6recycler.get(posiTion2).getCantidad_modelo();
                cantidad_AlcanceMax = ranking6recycler.get(posiTion2).getCantidad_AlcanceMax();
                cantidad_AlcanceMin= ranking6recycler.get(posiTion2).getCantidad_AlcanceMin();
                cantidad_anoAprobacion= ranking6recycler.get(posiTion2).getCantidad_anoAprobacion();
                cantidad_eod= ranking6recycler.get(posiTion2).getCantidad_eod();
                cantidad_TipoInstrumento= ranking6recycler.get(posiTion2).getCantidad_TipoInstrumento();
                cantidad_TipoVisita= ranking6recycler.get(posiTion2).getCantidad_TipoVisita();
                cantidad_costo= ranking6recycler.get(posiTion2).getCantidad_costo();
                cantidad_numeroSerie= ranking6recycler.get(posiTion2).getCantidad_numeroSerie();
                ((Mapa)context).editarBascula(cantidad_numero_aprobacion,cantidad_marca,cantidad_modelo,cantidad_AlcanceMax,cantidad_AlcanceMin,cantidad_anoAprobacion,cantidad_eod,cantidad_TipoInstrumento,cantidad_TipoVisita,cantidad_costo,cantidad_numeroSerie);
            }
        });
    }

    @Override
    public int getItemCount(){
        return ranking6recycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView cant_numero_aprobacion,cant_marca,cant_modelo,cant_AlcanceMax,cant_AlcanceMin,cant_CodigoMarca,cant_CodigoModelo,
                cant_anoAprobacion,cant_eod,cant_TipoInstrumento,cant_claseExactitud,cant_marcoPesas,cant_pesas5kg,cant_pesas10kg,
                cant_pesas20kg,cant_pesaClase_exactitud,cant_horario,cant_TipoVisita,cant_costo,cant_numeroSerie,cantidad_numero_folio;
        LinearLayout marco5;


        public ViewHolderRecycler(View itemView) {
            super(itemView);
            cant_numero_aprobacion =(TextView)itemView.findViewById(R.id.cantidad_numero_aprobacion);
            cant_marca =(TextView)itemView.findViewById(R.id.cantidad_marca);
            cant_modelo =(TextView)itemView.findViewById(R.id.cantidad_modelo);
            cant_AlcanceMax =(TextView)itemView.findViewById(R.id.cantidad_AlcanceMax);
            cant_AlcanceMin =(TextView)itemView.findViewById(R.id.cantidad_AlcanceMin);
            cant_CodigoMarca =(TextView)itemView.findViewById(R.id.cantidad_CodigoMarca);
            cant_CodigoModelo =(TextView)itemView.findViewById(R.id.cantidad_CodigoModelo);
            cant_anoAprobacion =(TextView)itemView.findViewById(R.id.cantidad_anoAprobacion);
            cant_eod =(TextView)itemView.findViewById(R.id.cantidad_eod);
            cant_TipoInstrumento =(TextView)itemView.findViewById(R.id.cantidad_TipoInstrumento);
            cant_claseExactitud =(TextView)itemView.findViewById(R.id.cantidad_claseExactitud);
            cant_marcoPesas =(TextView)itemView.findViewById(R.id.cantidad_marcoPesas);
            cant_pesas5kg =(TextView)itemView.findViewById(R.id.cantidad_pesas5kg);
            cant_pesas10kg =(TextView)itemView.findViewById(R.id.cantidad_pesas10kg);
            cant_pesas20kg =(TextView)itemView.findViewById(R.id.cantidad_pesas20kg);
            cant_pesaClase_exactitud =(TextView)itemView.findViewById(R.id.cantidad_pesaClase_exactitud);
            cant_horario =(TextView)itemView.findViewById(R.id.cantidad_horario);
            cant_costo =(TextView)itemView.findViewById(R.id.cantidad_costo);
            cant_TipoVisita =(TextView)itemView.findViewById(R.id.cantidad_TipoVisita);
            cant_costo =(TextView)itemView.findViewById(R.id.cantidad_costo);
            cant_numeroSerie =(TextView)itemView.findViewById(R.id.cantidad_numeroSerie);
            cantidad_numero_folio=itemView.findViewById(R.id.cantidad_numero_folio);
            marco5=(LinearLayout)itemView.findViewById(R.id.marco5);




        }
    }
}