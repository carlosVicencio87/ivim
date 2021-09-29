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

public class AdapterAlcanceMax extends RecyclerView.Adapter<AdapterAlcanceMax.ViewHolderRecycler>{
    private ArrayList<AlcanceRecycler> ranking3recycler;
    ViewHolderRecycler viewholderAlcanceMax;
    private  RecyclerView recyclerView;
    private Context context;
    private String alcance_max_bascula;

    public AdapterAlcanceMax(Mapa activity, int item, ArrayList<AlcanceRecycler> ranking3recycler, Resources resources)
    {
        this.ranking3recycler=ranking3recycler;
    }
    @Override
    public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item3,parent,false);
        context=parent.getContext();
        return new ViewHolderRecycler(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAlcanceMax.ViewHolderRecycler holder, int position) {
        viewholderAlcanceMax =holder;
        alcance_max_bascula = ranking3recycler.get(position).getAlcance_bascula();

        holder.alcanc_bascula.setText(alcance_max_bascula);
        holder.alcanc_bascula.setText(alcance_max_bascula);
    }

    @Override
    public int getItemCount(){
        return ranking3recycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView alcanc_bascula;


        public ViewHolderRecycler(View itemView) {
            super(itemView);
            alcanc_bascula =(TextView)itemView.findViewById(R.id.alcance_max);




        }
    }
}