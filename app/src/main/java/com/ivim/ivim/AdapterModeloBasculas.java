package com.ivim.ivim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterModeloBasculas extends RecyclerView.Adapter<AdapterModeloBasculas.ViewHolderRecycler>{
    private ArrayList<ModeloRecycler> rankingrecycler;
    ViewHolderRecycler viewholderModelo;
    private  RecyclerView recyclerView;
    private Context context;
    private String modelo_bascula;

    public AdapterModeloBasculas(ArrayList<ModeloRecycler> rankingrecycler)
    {
        this.rankingrecycler=rankingrecycler;
    }
    @Override
    public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        context=parent.getContext();
        return new ViewHolderRecycler(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterModeloBasculas.ViewHolderRecycler holder, int position) {
        viewholderModelo=holder;
        modelo_bascula=rankingrecycler.get(position).getModeloBascula();

        holder.model_bascula.setText(modelo_bascula);
    }

    @Override
    public int getItemCount(){
        return rankingrecycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView model_bascula;


        public ViewHolderRecycler(View itemView) {
            super(itemView);
            model_bascula=(TextView)itemView.findViewById(R.id.nombre_bascula);

            ;



        }
    }
}
