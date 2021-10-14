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
    private String cantidad_bascula;
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
        holder.cant_bascula.setText(cantidad_bascula);
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
                    anterior.cant_bascula.setTextColor(context.getResources().getColor(R.color.negro));
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
        TextView cant_bascula;
        LinearLayout marco5;


        public ViewHolderRecycler(View itemView) {
            super(itemView);
            cant_bascula =(TextView)itemView.findViewById(R.id.cantidad_bascula);
            marco5=(LinearLayout)itemView.findViewById(R.id.marco5);




        }
    }
}