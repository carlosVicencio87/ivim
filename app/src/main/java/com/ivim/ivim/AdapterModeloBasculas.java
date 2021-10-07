package com.ivim.ivim;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterModeloBasculas extends RecyclerView.Adapter<AdapterModeloBasculas.ViewHolderRecycler>{
    private ArrayList<ModeloRecycler> ranking2recycler;
    ViewHolderRecycler viewholderModelo;
    private  RecyclerView recyclerView;
    private Context context;
    private String modelo_bascula;

    public AdapterModeloBasculas(Mapa activity, int item, ArrayList<ModeloRecycler> ranking2recycler, Resources resources)
    {
        this.ranking2recycler=ranking2recycler;
    }
    @Override
    public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);
        context=parent.getContext();
        return new ViewHolderRecycler(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterModeloBasculas.ViewHolderRecycler holder, int position) {
        viewholderModelo =holder;
        modelo_bascula = ranking2recycler.get(position).getModelo_bascula();
        holder.model_bascula.setText(modelo_bascula);
        holder.model_bascula.setText(modelo_bascula);
        final String modelo  =  holder.model_bascula.getText().toString();
        holder.marco.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context,modelo,Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount(){
        return ranking2recycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView model_bascula;
        LinearLayout marco;


        public ViewHolderRecycler(View itemView) {
            super(itemView);
            model_bascula =(TextView)itemView.findViewById(R.id.modelo_bascula);
            marco=(LinearLayout)itemView.findViewById(R.id.marco);




        }
    }
}