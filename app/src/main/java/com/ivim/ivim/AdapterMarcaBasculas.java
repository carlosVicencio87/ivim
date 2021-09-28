package com.ivim.ivim;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMarcaBasculas extends RecyclerView.Adapter<AdapterMarcaBasculas.ViewHolderRecycler>{
    private ArrayList<MarcaRecycler> rankingrecycler;
    ViewHolderRecycler viewholderMarca;
    private  RecyclerView recyclerView;
    private Context context;
    private String marca_bascula;

    public AdapterMarcaBasculas(Mapa activity, int item, ArrayList<MarcaRecycler> rankingrecycler, Resources resources)
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
    public void onBindViewHolder(@NonNull AdapterMarcaBasculas.ViewHolderRecycler holder, int position) {
        viewholderMarca =holder;
        marca_bascula =rankingrecycler.get(position).getMarca_basculaBascula();

        holder.mark_bascula.setText(marca_bascula);
        holder.mark_bascula.setText(marca_bascula);
    }

    @Override
    public int getItemCount(){
        return rankingrecycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView mark_bascula;


        public ViewHolderRecycler(View itemView) {
            super(itemView);
            mark_bascula =(TextView)itemView.findViewById(R.id.marca_bascula);




        }
    }
}
